package com.volunteer.actionservice.application;

import com.volunteer.actionservice.adapters.out.ParticipationClient;
import com.volunteer.actionservice.adapters.out.UserClient;
import com.volunteer.actionservice.adapters.in.rest.representation.ActionNotificationRequest;
import com.volunteer.actionservice.adapters.in.rest.representation.ActionRequest;
import com.volunteer.actionservice.adapters.in.rest.representation.ActionResponse;
import com.volunteer.actionservice.adapters.in.rest.representation.ParticipationUpdateRequest;
import com.volunteer.actionservice.adapters.in.rest.representation.ProductResponse;
import com.volunteer.actionservice.application.domain.Action;
import com.volunteer.actionservice.application.domain.ActionProduct;
import com.volunteer.actionservice.application.domain.ActionStatus;
import com.volunteer.actionservice.application.domain.ActionType;
import com.volunteer.actionservice.application.domain.ActivismAction;
import com.volunteer.actionservice.application.domain.ContributeAction;
import com.volunteer.actionservice.application.domain.DonationAction;
import com.volunteer.actionservice.application.domain.FundingAction;
import com.volunteer.actionservice.application.ports.in.ActionUseCase;
import com.volunteer.actionservice.application.ports.out.ActionRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@ApplicationScoped
public class ActionService implements ActionUseCase {
    @Inject
    ActionRepository actionRepository;

    @ConfigProperty(name = "volunteer.actions.default-category", defaultValue = "community")
    String defaultCategory;

    @Inject
    @RestClient
    UserClient userClient;

    @Inject
    @RestClient
    ParticipationClient participationClient;

    @Transactional
    public ActionResponse create(ActionRequest request) {
        normalizeDates(request);
        validateRequest(request);
        verifyOrganization(request.organizationId);
        Action action = newAction(request.type);
        apply(action, request);
        actionRepository.persist(action);
        return toResponse(action);
    }

    public List<ActionResponse> search(String category, String location, ActionType type,
                                       ActionStatus status, LocalDateTime from, LocalDateTime to, Long organizationId) {
        String categoryFilter = normalize(category);
        String locationFilter = normalize(location);
        return actionRepository.listAll().stream()
                .filter(action -> categoryFilter == null || action.category.toLowerCase(Locale.ROOT).contains(categoryFilter))
                .filter(action -> locationFilter == null || action.location.toLowerCase(Locale.ROOT).contains(locationFilter))
                .filter(action -> type == null || matchesType(action, type))
                .filter(action -> status == null || action.status == status)
                .filter(action -> from == null || !action.endDate.isBefore(from))
                .filter(action -> to == null || !action.startDate.isAfter(to))
                .filter(action -> organizationId == null || Objects.equals(action.organizationId, organizationId))
                .map(this::toResponse)
                .toList();
    }

    public List<ActionResponse> listAll() {
        return search(null, null, null, null, null, null, null);
    }

    public ActionResponse get(Long id) {
        return toResponse(findAction(id));
    }

    @Transactional
    public ActionResponse update(Long id, ActionRequest request) {
        normalizeDates(request);
        validateRequest(request);
        Action existing = findAction(id);
        ensureOpen(existing);
        ensureCanChange(existing);
        if (!matchesType(existing, request.type)) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Action type cannot be changed");
        }
        if (request.organizationId != null && !Objects.equals(existing.organizationId, request.organizationId)) {
            verifyOrganization(request.organizationId);
        }
        apply(existing, request);
        notifyActionUpdated(existing);
        return toResponse(existing);
    }

    @Transactional
    public ActionResponse cancel(Long id) {
        Action action = findAction(id);
        ensureOpen(action);
        ensureCanChange(action);
        action.status = ActionStatus.CANCELLED;
        notifyActionCancelled(action);
        return toResponse(action);
    }

    @Transactional
    public ActionResponse complete(Long id) {
        Action action = findAction(id);
        ensureOpen(action);
        action.status = ActionStatus.COMPLETED;
        return toResponse(action);
    }

    @Scheduled(every = "5m")
    @Transactional
    public void autoCancelExpiredActivismActions() {
        LocalDateTime now = LocalDateTime.now();
        actionRepository.listAll().stream()
                .filter(action -> action instanceof ActivismAction)
                .filter(action -> action.status == ActionStatus.OPEN)
                .filter(action -> action.endDate != null && action.endDate.isBefore(now))
                .map(action -> (ActivismAction) action)
                .filter(activism -> value(activism.currentParticipants) < value(activism.minParticipants))
                .forEach(activism -> {
                    activism.status = ActionStatus.CANCELLED;
                    try {
                        participationClient.actionCancelled(new ActionNotificationRequest(
                                activism.id, activism.title,
                                "Action automatically cancelled due to insufficient participants"));
                    } catch (ProcessingException | WebApplicationException ignored) {
                    }
                });
    }

    public ActionResponse availability(Long id) {
        Action action = findAction(id);
        ActionResponse response = toResponse(action);
        response.available = isAvailable(action);
        return response;
    }

    public List<ActionResponse> findByOrganization(Long organizationId) {
        return actionRepository.listAll().stream()
                .filter(action -> Objects.equals(action.organizationId, organizationId))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ActionResponse participationAccepted(Long id, ParticipationUpdateRequest request) {
        Action action = findAction(id);
        ensureAvailable(action);
        action.currentParticipants = value(action.currentParticipants) + 1;
        applyContribution(action, request, false);
        return toResponse(action);
    }

    @Transactional
    public ActionResponse participationCancelled(Long id, ParticipationUpdateRequest request) {
        Action action = findAction(id);
        action.currentParticipants = Math.max(0, value(action.currentParticipants) - 1);
        applyContribution(action, request, true);
        return toResponse(action);
    }

    private Action findAction(Long id) {
        return actionRepository.findByIdOptional(id)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "Action not found"));
    }

    private Action newAction(ActionType type) {
        return switch (type) {
            case ACTIVISM -> new ActivismAction();
            case CONTRIBUTE -> new ContributeAction();
            case DONATION -> new DonationAction();
            case FUNDING -> new FundingAction();
        };
    }

    private void apply(Action action, ActionRequest request) {
        if (request.organizationId != null) {
            action.organizationId = request.organizationId;
        }
        action.title = request.title;
        action.description = request.description;
        action.startDate = request.startDate;
        action.endDate = request.endDate;
        action.location = request.location;
        action.category = request.category == null || request.category.isBlank() ? defaultCategory : request.category;

        if (action instanceof ActivismAction activism) {
            activism.minParticipants = request.minParticipants;
            activism.maxParticipants = request.maxParticipants != null ? request.maxParticipants : request.totalParticipants;
        }
        if (action instanceof ContributeAction contribute) {
            contribute.requiredItems = request.requiredItems;
            contribute.products.clear();
            contribute.products.addAll(toProducts(request));
        }
        if (action instanceof DonationAction donation) {
            donation.requiredItems = request.requiredItems;
            donation.products.clear();
            donation.products.addAll(toProducts(request));
        }
        if (action instanceof FundingAction funding) {
            funding.targetAmount = request.targetAmount;
            if (funding.raisedAmount == null) {
                funding.raisedAmount = BigDecimal.ZERO;
            }
        }
    }

    private void validateRequest(ActionRequest request) {
        if (request.type == null) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Action type is required");
        }
        if (request.startDate == null || request.endDate == null) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Start and end date are required");
        }
        if (!request.endDate.isAfter(request.startDate)) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "End date must be after start date");
        }
        if (request.type == ActionType.ACTIVISM) {
            Integer maxParticipants = request.maxParticipants != null ? request.maxParticipants : request.totalParticipants;
            if (request.minParticipants == null || maxParticipants == null || request.minParticipants < 1
                    || maxParticipants < request.minParticipants) {
                throw new ServiceException(Response.Status.BAD_REQUEST, "Invalid activism participant limits");
            }
        }
        if (request.type == ActionType.CONTRIBUTE
                || request.type == ActionType.DONATION) {
            if ((request.requiredItems == null || request.requiredItems.isBlank())
                    && (request.products == null || request.products.isEmpty())) {
                throw new ServiceException(Response.Status.BAD_REQUEST, "Contribute actions require items");
            }
        }
        if (request.type == ActionType.FUNDING
                && (request.targetAmount == null || request.targetAmount.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Funding actions require a positive target amount");
        }
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.toLowerCase(Locale.ROOT);
    }

    private void normalizeDates(ActionRequest request) {
        if (request.startDate == null) {
            request.startDate = request.startAt;
        }
        if (request.endDate == null) {
            request.endDate = request.endAt;
        }
    }

    private void verifyOrganization(Long organizationId) {
        if (organizationId == null) {
            return;
        }
        try (Response response = userClient.organizationExists(organizationId)) {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new ServiceException(Response.Status.NOT_FOUND, "Organization not found");
            }
            if (response.getStatus() >= 400) {
                throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "User service unavailable");
            }
        } catch (WebApplicationException exception) {
            if (exception.getResponse() != null
                    && exception.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new ServiceException(Response.Status.NOT_FOUND, "Organization not found");
            }
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "User service unavailable");
        } catch (ProcessingException exception) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "User service unavailable");
        }
    }

    private boolean matchesType(Action action, ActionType type) {
        return action.type() == type;
    }

    private boolean isAvailable(Action action) {
        return action.status == ActionStatus.OPEN
                && LocalDateTime.now().isBefore(action.endDate)
                && (!(action instanceof ActivismAction activism)
                || activism.maxParticipants == null
                || value(action.currentParticipants) < activism.maxParticipants);
    }

    private void ensureAvailable(Action action) {
        if (!isAvailable(action)) {
            throw new ServiceException(Response.Status.CONFLICT, "Action is not available");
        }
    }

    private void ensureOpen(Action action) {
        if (action.status != ActionStatus.OPEN) {
            throw new ServiceException(Response.Status.CONFLICT, "Action is not open");
        }
    }

    private void ensureCanChange(Action action) {
        if (LocalDateTime.now().plusHours(12).isAfter(action.startDate)) {
            throw new ServiceException(Response.Status.CONFLICT, "Action cannot be changed less than 12 hours before start");
        }
    }

    private void notifyActionUpdated(Action action) {
        try {
            participationClient.actionUpdated(new ActionNotificationRequest(action.id, action.title, "Action updated"));
        } catch (ProcessingException | WebApplicationException ignored) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "Participation service unavailable");
        }
    }

    private void notifyActionCancelled(Action action) {
        try {
            participationClient.actionCancelled(new ActionNotificationRequest(action.id, action.title, "Action cancelled"));
        } catch (ProcessingException | WebApplicationException ignored) {
            throw new ServiceException(Response.Status.SERVICE_UNAVAILABLE, "Participation service unavailable");
        }
    }

    private void applyContribution(Action action, ParticipationUpdateRequest request, boolean cancelled) {
        if (request == null) {
            return;
        }
        if (action instanceof FundingAction funding && request.amount != null) {
            BigDecimal current = funding.raisedAmount == null ? BigDecimal.ZERO : funding.raisedAmount;
            funding.raisedAmount = cancelled ? current.subtract(request.amount) : current.add(request.amount);
            if (funding.raisedAmount.compareTo(BigDecimal.ZERO) < 0) {
                funding.raisedAmount = BigDecimal.ZERO;
            }
        }
        if (action instanceof ContributeAction contribute && request.products != null) {
            updateProductQuantities(contribute.products, request, cancelled);
        }
        if (action instanceof DonationAction donation && request.products != null) {
            updateProductQuantities(donation.products, request, cancelled);
        }
    }

    private void updateProductQuantities(List<ActionProduct> products, ParticipationUpdateRequest request, boolean cancelled) {
        for (var contribution : request.products) {
            if (contribution.name == null || contribution.quantity == null) {
                continue;
            }
            products.stream()
                    .filter(product -> contribution.name.equalsIgnoreCase(product.name))
                    .findFirst()
                    .ifPresent(product -> {
                        int current = value(product.remainingQuantity);
                        product.remainingQuantity = cancelled
                                ? current + contribution.quantity
                                : Math.max(0, current - contribution.quantity);
                    });
        }
    }

    private List<ActionProduct> toProducts(ActionRequest request) {
        if (request.products == null) {
            return List.of();
        }
        return request.products.stream()
                .map(productRequest -> {
                    ActionProduct product = new ActionProduct();
                    product.name = productRequest.name;
                    product.targetQuantity = productRequest.targetQuantity;
                    product.remainingQuantity = productRequest.targetQuantity;
                    return product;
                })
                .toList();
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }

    public ActionResponse toResponse(Action action) {
        ActionResponse response = new ActionResponse();
        response.id = action.id;
        response.type = action.type();
        response.organizationId = action.organizationId;
        response.title = action.title;
        response.description = action.description;
        response.startDate = action.startDate;
        response.endDate = action.endDate;
        response.startAt = action.startDate;
        response.endAt = action.endDate;
        response.location = action.location;
        response.category = action.category;
        response.status = action.status;
        response.currentParticipants = action.currentParticipants;
        if (action instanceof ActivismAction activism) {
            response.minParticipants = activism.minParticipants;
            response.maxParticipants = activism.maxParticipants;
            response.totalParticipants = activism.maxParticipants;
        }
        if (action instanceof ContributeAction contribute) {
            response.requiredItems = contribute.requiredItems;
            response.products = toProductResponses(contribute.products);
        }
        if (action instanceof DonationAction donation) {
            response.requiredItems = donation.requiredItems;
            response.products = toProductResponses(donation.products);
        }
        if (action instanceof FundingAction funding) {
            response.targetAmount = funding.targetAmount;
            response.raisedAmount = funding.raisedAmount;
        }
        response.available = isAvailable(action);
        return response;
    }

    private List<ProductResponse> toProductResponses(List<ActionProduct> products) {
        return products.stream()
                .map(product -> {
                    ProductResponse response = new ProductResponse();
                    response.name = product.name;
                    response.targetQuantity = product.targetQuantity;
                    response.remainingQuantity = product.remainingQuantity;
                    return response;
                })
                .toList();
    }
}
