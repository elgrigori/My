package com.volunteer.participationservice.application;

import com.volunteer.participationservice.adapters.in.rest.representation.ActionCounterRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionNotificationRequest;
import com.volunteer.participationservice.adapters.out.ActionClient;
import com.volunteer.participationservice.adapters.out.UserClient;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationResponse;
import com.volunteer.participationservice.adapters.in.rest.representation.ProductContributionRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import com.volunteer.participationservice.application.domain.Participation;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import com.volunteer.participationservice.application.ports.in.ParticipationUseCase;
import com.volunteer.participationservice.application.ports.out.ParticipationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ParticipationService implements ParticipationUseCase {
    @Inject
    ParticipationRepository participationRepository;

    @Inject
    NotificationService notificationService;

    @Inject
    @RestClient
    UserClient userClient;

    @Inject
    @RestClient
    ActionClient actionClient;

    @Transactional
    public ParticipationResponse create(ParticipationRequest request) {
        UserSummary volunteer = fetchVolunteer(request.volunteerId);
        if (!"VOLUNTEER".equals(volunteer.type)) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Participation requires a volunteer user");
        }
        ActionSummary action = fetchAction(request.actionId);
        validateActionAvailable(action);
        if (!participationRepository.findOverlapping(request.volunteerId, action.startDate, action.endDate).isEmpty()) {
            throw new ServiceException(Response.Status.CONFLICT, "Volunteer already participates in an overlapping action");
        }

        Participation participation = new Participation();
        participation.volunteerId = request.volunteerId;
        participation.actionId = request.actionId;
        participation.type = request.type != null ? request.type : action.type;
        participation.amount = request.amount;
        participation.productsSummary = summarizeProducts(request);
        participation.startDate = action.startDate;
        participation.endDate = action.endDate;
        participation.status = ParticipationStatus.CONFIRMED;
        appendNotification(participation, notificationService.confirmation(volunteer, action));
        participationRepository.persist(participation);
        updateActionAccepted(participation, request);
        return toResponse(participation);
    }

    @Transactional
    public ParticipationResponse cancel(Long id) {
        Participation participation = participationRepository.findByIdOptional(id)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "Participation not found"));
        if (participation.status == ParticipationStatus.CANCELLED) {
            throw new ServiceException(Response.Status.CONFLICT, "Participation is already cancelled");
        }
        validateCanCancel(fetchAction(participation.actionId));
        participation.status = ParticipationStatus.CANCELLED;
        appendNotification(participation, "Participation cancelled");
        updateActionCancelled(participation);
        return toResponse(participation);
    }

    public List<ParticipationResponse> findByVolunteer(Long volunteerId) {
        return participationRepository.findConfirmedByVolunteer(volunteerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ParticipationResponse> search(Long volunteerId, Long actionId, ParticipationStatus status) {
        return participationRepository.search(volunteerId, actionId, status).stream()
                .map(this::toResponse)
                .toList();
    }

    public ParticipationResponse get(Long id) {
        return toResponse(participationRepository.findByIdOptional(id)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "Participation not found")));
    }

    public List<ParticipationResponse> findByAction(Long actionId) {
        return participationRepository.findByAction(actionId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ParticipationResponse> notificationsForVolunteer(Long volunteerId) {
        return participationRepository.findNotificationsForVolunteer(volunteerId).stream()
                .flatMap(participation -> notificationResponses(participation).stream())
                .toList();
    }

    @Transactional
    public ParticipationResponse markNotificationRead(Long notificationId) {
        Participation participation = participationRepository.findByIdOptional(notificationId)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "Notification not found"));
        participation.notificationRead = true;
        return toResponse(participation);
    }

    @Transactional
    public void actionUpdated(ActionNotificationRequest request) {
        notifyActionParticipants(request, "Action updated");
    }

    @Transactional
    public void actionCancelled(ActionNotificationRequest request) {
        notifyActionParticipants(request, "Action cancelled");
    }

    private UserSummary fetchVolunteer(Long volunteerId) {
        try {
            UserSummary volunteer = userClient.getUser(volunteerId);
            if (volunteer == null || volunteer.id == null) {
                throw new ServiceException(Response.Status.NOT_FOUND, "Volunteer not found");
            }
            return volunteer;
        } catch (WebApplicationException exception) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Volunteer not found");
        } catch (ProcessingException exception) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "User service unavailable");
        }
    }

    private ActionSummary fetchAction(Long actionId) {
        try {
            ActionSummary action = actionClient.getAction(actionId);
            if (action == null || action.id == null) {
                throw new ServiceException(Response.Status.NOT_FOUND, "Action not found");
            }
            normalizeDates(action);
            return action;
        } catch (WebApplicationException exception) {
            throw new ServiceException(Response.Status.NOT_FOUND, "Action not found");
        } catch (ProcessingException exception) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "Action service unavailable");
        }
    }

    private void validateActionAvailable(ActionSummary action) {
        if (action.status != null && !"OPEN".equals(action.status)) {
            throw new ServiceException(Response.Status.CONFLICT, "Action is not open");
        }
        if (Boolean.FALSE.equals(action.available)) {
            throw new ServiceException(Response.Status.CONFLICT, "Action is not available");
        }
        if (action.endDate != null && !LocalDateTime.now().isBefore(action.endDate)) {
            throw new ServiceException(Response.Status.CONFLICT, "Action has ended");
        }
    }

    private void validateCanCancel(ActionSummary action) {
        if (action.startDate != null && LocalDateTime.now().plusHours(12).isAfter(action.startDate)) {
            throw new ServiceException(Response.Status.CONFLICT, "Participation cannot be cancelled less than 12 hours before action start");
        }
    }

    private void normalizeDates(ActionSummary action) {
        if (action.startDate == null) {
            action.startDate = action.startAt;
        }
        if (action.endDate == null) {
            action.endDate = action.endAt;
        }
    }

    private void updateActionAccepted(Participation participation, ParticipationRequest request) {
        try {
            actionClient.participationAccepted(participation.actionId, toCounterRequest(request));
        } catch (ProcessingException | WebApplicationException exception) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "Action service unavailable");
        }
    }

    private void updateActionCancelled(Participation participation) {
        try {
            ActionCounterRequest request = new ActionCounterRequest();
            request.amount = participation.amount;
            request.products = parseProducts(participation.productsSummary);
            actionClient.participationCancelled(participation.actionId, request);
        } catch (ProcessingException | WebApplicationException exception) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "Action service unavailable");
        }
    }

    private ActionCounterRequest toCounterRequest(ParticipationRequest request) {
        ActionCounterRequest counterRequest = new ActionCounterRequest();
        counterRequest.amount = request.amount;
        counterRequest.products = request.products;
        return counterRequest;
    }

    private String summarizeProducts(ParticipationRequest request) {
        if (request.products == null || request.products.isEmpty()) {
            return null;
        }
        return request.products.stream()
                .map(product -> {
                    String name = product.name != null ? product.name : String.valueOf(product.productId);
                    return name + ":" + product.quantity;
                })
                .collect(Collectors.joining(","));
    }

    private List<ProductContributionRequest> parseProducts(String productsSummary) {
        if (productsSummary == null || productsSummary.isBlank()) {
            return null;
        }
        return java.util.Arrays.stream(productsSummary.split(","))
                .map(item -> item.split(":"))
                .filter(parts -> parts.length == 2)
                .map(parts -> {
                    ProductContributionRequest product = new ProductContributionRequest();
                    product.name = parts[0];
                    product.quantity = Integer.valueOf(parts[1]);
                    return product;
                })
                .toList();
    }

    private void notifyActionParticipants(ActionNotificationRequest request, String fallbackMessage) {
        if (request == null || request.actionId == null) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Action id is required");
        }
        String message = request.message != null ? request.message : fallbackMessage;
        participationRepository.findAcceptedByAction(request.actionId).forEach(participation -> {
            appendNotification(participation, message);
        });
    }

    private void appendNotification(Participation participation, String message) {
        participation.notificationMessage = message;
        participation.notificationRead = false;
        participation.notificationHistory = participation.notificationHistory == null || participation.notificationHistory.isBlank()
                ? message
                : participation.notificationHistory + "\n" + message;
    }

    private List<ParticipationResponse> notificationResponses(Participation participation) {
        if (participation.notificationHistory == null || participation.notificationHistory.isBlank()) {
            return List.of(toResponse(participation));
        }
        return java.util.Arrays.stream(participation.notificationHistory.split("\\R"))
                .filter(message -> !message.isBlank())
                .map(message -> {
                    ParticipationResponse response = toResponse(participation);
                    response.notificationMessage = message;
                    return response;
                })
                .toList();
    }

    public ParticipationResponse toResponse(Participation participation) {
        ParticipationResponse response = new ParticipationResponse();
        response.id = participation.id;
        response.type = participation.type;
        response.volunteerId = participation.volunteerId;
        response.actionId = participation.actionId;
        response.amount = participation.amount;
        response.productsSummary = participation.productsSummary;
        response.startDate = participation.startDate;
        response.endDate = participation.endDate;
        response.status = participation.status;
        response.notificationMessage = participation.notificationMessage;
        response.notificationRead = participation.notificationRead;
        return response;
    }
}
