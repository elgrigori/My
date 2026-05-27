package com.volunteer.participationservice;

import com.volunteer.participationservice.adapters.out.ActionClient;
import com.volunteer.participationservice.adapters.out.UserClient;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import com.volunteer.participationservice.adapters.out.ParticipationRepository;
import com.volunteer.participationservice.application.ParticipationService;
import com.volunteer.participationservice.application.ServiceException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
class ParticipationServiceTest {
    @Inject
    ParticipationService participationService;

    @Inject
    ParticipationRepository participationRepository;

    @InjectMock
    @RestClient
    UserClient userClient;

    @InjectMock
    @RestClient
    ActionClient actionClient;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        participationRepository.deleteAll();
    }

    @Test
    void createsParticipationAndNotification() {
        when(userClient.getUser(11L)).thenReturn(volunteer(11L));
        when(actionClient.getAction(21L)).thenReturn(action(21L, "Beach cleanup",
                LocalDateTime.of(2026, 6, 1, 10, 0),
                LocalDateTime.of(2026, 6, 1, 13, 0)));

        ParticipationRequest request = request(11L, 21L);
        var response = participationService.create(request);

        assertEquals(ParticipationStatus.CONFIRMED, response.status);
        assertEquals("Confirmation sent to volunteer11@example.com for action Beach cleanup", response.notificationMessage);
    }

    @Test
    void rejectsOverlappingParticipation() {
        when(userClient.getUser(12L)).thenReturn(volunteer(12L));
        when(actionClient.getAction(31L)).thenReturn(action(31L, "Morning action",
                LocalDateTime.of(2026, 6, 3, 9, 0),
                LocalDateTime.of(2026, 6, 3, 12, 0)));
        when(actionClient.getAction(32L)).thenReturn(action(32L, "Late morning action",
                LocalDateTime.of(2026, 6, 3, 11, 0),
                LocalDateTime.of(2026, 6, 3, 14, 0)));

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
    void notifiesParticipantsOnActionUpdated() {
        when(userClient.getUser(16L)).thenReturn(volunteer(16L));
        when(actionClient.getAction(71L)).thenReturn(action(71L, "Park cleanup",
                LocalDateTime.of(2026, 10, 1, 9, 0),
                LocalDateTime.of(2026, 10, 1, 11, 0)));

        participationService.create(request(16L, 71L));

        var notifRequest = new com.volunteer.participationservice.adapters.in.rest.representation.ActionNotificationRequest();
        notifRequest.actionId = 71L;
        participationService.actionUpdated(notifRequest);

        var notifications = participationService.notificationsForVolunteer(16L);
        assertEquals(2, notifications.size());
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
