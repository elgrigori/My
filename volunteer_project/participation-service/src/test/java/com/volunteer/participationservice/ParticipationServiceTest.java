package com.volunteer.participationservice;

import com.volunteer.participationservice.adapters.out.ActionClient;
import com.volunteer.participationservice.adapters.out.UserClient;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionCounterRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionNotificationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import com.volunteer.participationservice.application.ParticipationService;
import com.volunteer.participationservice.application.ServiceException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;

import static com.volunteer.participationservice.fixture.ParticipationFixture.contributeRequest;
import static com.volunteer.participationservice.fixture.ParticipationFixture.fundingRequest;
import static com.volunteer.participationservice.fixture.ParticipationFixture.product;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class ParticipationServiceTest extends IntegrationBase {
    @Inject
    ParticipationService participationService;

    @InjectMock
    @RestClient
    UserClient userClient;

    @InjectMock
    @RestClient
    ActionClient actionClient;

    @Test
    void createsParticipationAndNotification() {
        when(userClient.getUser(11L)).thenReturn(volunteer(11L));
        LocalDateTime start = LocalDateTime.now().plusDays(10);
        when(actionClient.getAction(21L)).thenReturn(action(21L, "Beach cleanup",
                start,
                start.plusHours(3)));

        ParticipationRequest request = request(11L, 21L);
        var response = participationService.create(request);

        assertEquals(ParticipationStatus.CONFIRMED, response.status);
        assertEquals("Confirmation sent to volunteer11@example.com for action Beach cleanup", response.notificationMessage);
    }

    @Test
    void rejectsOverlappingParticipation() {
        when(userClient.getUser(12L)).thenReturn(volunteer(12L));
        LocalDateTime start = LocalDateTime.now().plusDays(11);
        when(actionClient.getAction(31L)).thenReturn(action(31L, "Morning action",
                start,
                start.plusHours(3)));
        when(actionClient.getAction(32L)).thenReturn(action(32L, "Late morning action",
                start.plusHours(2),
                start.plusHours(5)));

        participationService.create(request(12L, 31L));

        assertThrows(ServiceException.class, () -> participationService.create(request(12L, 32L)));
    }

    @Test
    void rejectsCancellationWithin12Hours() {
        when(userClient.getUser(13L)).thenReturn(volunteer(13L));
        LocalDateTime startingSoon = LocalDateTime.now().plusHours(6);
        when(actionClient.getAction(41L)).thenReturn(action(41L, "Soon action",
                startingSoon,
                startingSoon.plusHours(2)));

        var response = participationService.create(request(13L, 41L));

        assertThrows(ServiceException.class, () -> participationService.cancel(response.id));
    }

    @Test
    void rejectsParticipationOnNonOpenAction() {
        when(userClient.getUser(14L)).thenReturn(volunteer(14L));
        ActionSummary cancelled = action(51L, "Cancelled action",
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 1, 12, 0));
        cancelled.status = "CANCELLED";
        when(actionClient.getAction(51L)).thenReturn(cancelled);

        assertThrows(ServiceException.class, () -> participationService.create(request(14L, 51L)));
    }

    @Test
    void rejectsParticipationWhenActionIsUnavailable() {
        when(userClient.getUser(21L)).thenReturn(volunteer(21L));
        ActionSummary action = action(121L, "Full action",
                LocalDateTime.now().plusDays(8),
                LocalDateTime.now().plusDays(8).plusHours(2));
        action.available = false;
        when(actionClient.getAction(121L)).thenReturn(action);

        ServiceException exception = assertThrows(ServiceException.class,
                () -> participationService.create(request(21L, 121L)));

        assertEquals(Response.Status.CONFLICT, exception.status());
    }

    @Test
    void rejectsNonVolunteerUser() {
        UserSummary orgUser = volunteer(17L);
        orgUser.type = "ORGANIZATION";
        when(userClient.getUser(17L)).thenReturn(orgUser);
        when(actionClient.getAction(81L)).thenReturn(action(81L, "Org action",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10).plusHours(2)));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> participationService.create(request(17L, 81L)));

        assertEquals(Response.Status.BAD_REQUEST, exception.status());
    }

    @Test
    void rejectsParticipationWhenActionAlreadyEnded() {
        when(userClient.getUser(18L)).thenReturn(volunteer(18L));
        when(actionClient.getAction(91L)).thenReturn(action(91L, "Past action",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1)));

        assertThrows(ServiceException.class, () -> participationService.create(request(18L, 91L)));
    }

    @Test
    void returnsServiceUnavailableWhenUserServiceUnavailable() {
        when(userClient.getUser(19L)).thenThrow(new ProcessingException("User service down"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> participationService.create(request(19L, 101L)));

        assertEquals(Response.Status.SERVICE_UNAVAILABLE, exception.status());
    }

    @Test
    void returnsServiceUnavailableWhenActionServiceUnavailable() {
        when(userClient.getUser(20L)).thenReturn(volunteer(20L));
        when(actionClient.getAction(111L)).thenThrow(new ProcessingException("Action service down"));

        ServiceException exception = assertThrows(ServiceException.class,
                () -> participationService.create(request(20L, 111L)));

        assertEquals(Response.Status.SERVICE_UNAVAILABLE, exception.status());
    }

    @Test
    void cancelsParticipationMoreThan12HoursAhead() {
        when(userClient.getUser(15L)).thenReturn(volunteer(15L));
        when(actionClient.getAction(61L)).thenReturn(action(61L, "Future action",
                LocalDateTime.of(2026, 9, 1, 10, 0),
                LocalDateTime.of(2026, 9, 1, 12, 0)));

        var response = participationService.create(request(15L, 61L));
        var cancelled = participationService.cancel(response.id);

        assertEquals(ParticipationStatus.CANCELLED, cancelled.status);
    }

    @Test
    void cancelsUsingStoredParticipationDate() {
        when(userClient.getUser(26L)).thenReturn(volunteer(26L));
        when(actionClient.getAction(126L)).thenReturn(action(126L, "Future action",
                LocalDateTime.now().plusDays(18),
                LocalDateTime.now().plusDays(18).plusHours(2)));

        var response = participationService.create(request(26L, 126L));
        clearInvocations(actionClient);

        var cancelled = participationService.cancel(response.id);

        assertEquals(ParticipationStatus.CANCELLED, cancelled.status);
        verify(actionClient, never()).getAction(126L);
    }

    @Test
    void rejectsCancellingAlreadyCancelledParticipation() {
        when(userClient.getUser(22L)).thenReturn(volunteer(22L));
        when(actionClient.getAction(122L)).thenReturn(action(122L, "Future action",
                LocalDateTime.now().plusDays(12),
                LocalDateTime.now().plusDays(12).plusHours(2)));

        var response = participationService.create(request(22L, 122L));
        participationService.cancel(response.id);

        ServiceException exception = assertThrows(ServiceException.class, () -> participationService.cancel(response.id));

        assertEquals(Response.Status.CONFLICT, exception.status());
    }

    @Test
    void sendsFundingAmountToActionCounters() {
        when(userClient.getUser(23L)).thenReturn(volunteer(23L));
        when(actionClient.getAction(123L)).thenReturn(action(123L, "Funding action",
                LocalDateTime.now().plusDays(14),
                LocalDateTime.now().plusDays(14).plusHours(2)));
        ParticipationRequest request = fundingRequest(23L, 123L, BigDecimal.valueOf(25));

        participationService.create(request);

        ArgumentCaptor<ActionCounterRequest> captor = ArgumentCaptor.forClass(ActionCounterRequest.class);
        verify(actionClient).participationAccepted(org.mockito.ArgumentMatchers.eq(123L), captor.capture());
        assertEquals(BigDecimal.valueOf(25), captor.getValue().amount);
    }

    @Test
    void sendsContributedProductsToActionCounters() {
        when(userClient.getUser(24L)).thenReturn(volunteer(24L));
        when(actionClient.getAction(124L)).thenReturn(action(124L, "Food collection",
                LocalDateTime.now().plusDays(15),
                LocalDateTime.now().plusDays(15).plusHours(2)));
        ParticipationRequest request = contributeRequest(24L, 124L, product(1L, "Rice", 5));

        participationService.create(request);

        ArgumentCaptor<ActionCounterRequest> captor = ArgumentCaptor.forClass(ActionCounterRequest.class);
        verify(actionClient).participationAccepted(org.mockito.ArgumentMatchers.eq(124L), captor.capture());
        assertEquals(1, captor.getValue().products.size());
        assertEquals("Rice", captor.getValue().products.get(0).name);
        assertEquals(5, captor.getValue().products.get(0).quantity);
    }

    @Test
    void sendsCancellationPayloadToActionCounters() {
        when(userClient.getUser(25L)).thenReturn(volunteer(25L));
        when(actionClient.getAction(125L)).thenReturn(action(125L, "Food collection",
                LocalDateTime.now().plusDays(16),
                LocalDateTime.now().plusDays(16).plusHours(2)));
        ParticipationRequest request = contributeRequest(25L, 125L, product(2L, "Milk", 3));

        var response = participationService.create(request);
        participationService.cancel(response.id);

        ArgumentCaptor<ActionCounterRequest> captor = ArgumentCaptor.forClass(ActionCounterRequest.class);
        verify(actionClient).participationCancelled(org.mockito.ArgumentMatchers.eq(125L), captor.capture());
        assertEquals(1, captor.getValue().products.size());
        assertEquals("Milk", captor.getValue().products.get(0).name);
        assertEquals(3, captor.getValue().products.get(0).quantity);
    }

    @Test
    void notifiesParticipantsOnActionUpdated() {
        when(userClient.getUser(16L)).thenReturn(volunteer(16L));
        when(actionClient.getAction(71L)).thenReturn(action(71L, "Park cleanup",
                LocalDateTime.of(2026, 10, 1, 9, 0),
                LocalDateTime.of(2026, 10, 1, 11, 0)));

        participationService.create(request(16L, 71L));

        var notifRequest = new ActionNotificationRequest();
        notifRequest.actionId = 71L;
        participationService.actionUpdated(notifRequest);

        var notifications = participationService.notificationsForVolunteer(16L);
        assertEquals(2, notifications.size());
    }

    @Test
    void rejectsActionNotificationWithoutActionId() {
        ServiceException exception = assertThrows(ServiceException.class,
                () -> participationService.actionUpdated(new ActionNotificationRequest()));

        assertEquals(Response.Status.BAD_REQUEST, exception.status());
    }

    private ParticipationRequest request(Long volunteerId, Long actionId) {
        ParticipationRequest request = new ParticipationRequest();
        request.volunteerId = volunteerId;
        request.actionId = actionId;
        return request;
    }

    private UserSummary volunteer(Long id) {
        UserSummary user = new UserSummary();
        user.id = id;
        user.type = "VOLUNTEER";
        user.username = "volunteer" + id;
        user.email = "volunteer" + id + "@example.com";
        return user;
    }

    private ActionSummary action(Long id, String title, LocalDateTime startDate, LocalDateTime endDate) {
        ActionSummary action = new ActionSummary();
        action.id = id;
        action.type = "ACTIVISM";
        action.title = title;
        action.startDate = startDate;
        action.endDate = endDate;
        return action;
    }
}
