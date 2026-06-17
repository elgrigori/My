package com.volunteer.actionservice;

import com.volunteer.actionservice.adapters.in.rest.representation.ActionRequest;
import com.volunteer.actionservice.application.domain.ActionType;
import com.volunteer.actionservice.adapters.out.ParticipationClient;
import com.volunteer.actionservice.application.ActionService;
import com.volunteer.actionservice.application.ServiceException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class ActionServiceTest extends IntegrationBase {
    @Inject
    ActionService actionService;

    @InjectMock
    @RestClient
    ParticipationClient participationClient;

    @Test
    void createsActivismAction() {
        var response = actionService.create(activism("Park cleanup", "Athens"));

        assertEquals(ActionType.ACTIVISM, response.type);
        assertEquals(5, response.minParticipants);
        assertEquals("environment", response.category);
    }

    @Test
    void rejectsInvalidDateRange() {
        ActionRequest request = activism("Bad dates", "Athens");
        request.endDate = request.startDate.minusHours(1);

        assertThrows(ServiceException.class, () -> actionService.create(request));
    }

    @Test
    void createsFundingAction() {
        ActionRequest request = new ActionRequest();
        request.type = ActionType.FUNDING;
        request.title = "School fund";
        request.description = "Raise money for schools";
        request.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        request.endDate = LocalDateTime.of(2026, 7, 31, 18, 0);
        request.location = "Athens";
        request.targetAmount = new java.math.BigDecimal("5000");

        var response = actionService.create(request);

        assertEquals(ActionType.FUNDING, response.type);
        assertEquals("School fund", response.title);
    }

    @Test
    void completesAction() {
        var created = actionService.create(activism("Cleanup", "Piraeus"));

        var completed = actionService.complete(created.id);

        assertEquals(com.volunteer.actionservice.application.domain.ActionStatus.COMPLETED, completed.status);
    }

    @Test
    void rejectsCompleteOnAlreadyCompletedAction() {
        var created = actionService.create(activism("Cleanup2", "Piraeus"));
        actionService.complete(created.id);

        assertThrows(ServiceException.class, () -> actionService.complete(created.id));
    }

    @Test
    void rejectsUpdateWithin12HoursOfStart() {
        ActionRequest request = activism("Last minute", "Athens");
        request.startDate = LocalDateTime.now().plusHours(6);
        request.endDate = LocalDateTime.now().plusHours(8);

        var created = actionService.create(request);

        ActionRequest update = activism("Updated title", "Athens");
        update.startDate = created.startDate;
        update.endDate = created.endDate;

        assertThrows(ServiceException.class, () -> actionService.update(created.id, update));
    }

    @Test
    void rejectsCancelWithin12HoursOfStart() {
        ActionRequest request = activism("Late cancel", "Athens");
        request.startDate = LocalDateTime.now().plusHours(6);
        request.endDate = LocalDateTime.now().plusHours(8);

        var created = actionService.create(request);

        assertThrows(ServiceException.class, () -> actionService.cancel(created.id));
    }

    @Test
    void getsActionById() {
        var created = actionService.create(activism("Retrievable", "Athens"));

        var retrieved = actionService.get(created.id);

        assertEquals(created.id, retrieved.id);
        assertEquals("Retrievable", retrieved.title);
    }

    @Test
    void cancelsActionMoreThan12HoursAhead() {
        ActionRequest request = activism("Cancellable", "Athens");
        request.startDate = LocalDateTime.of(2026, 9, 1, 10, 0);
        request.endDate = LocalDateTime.of(2026, 9, 1, 14, 0);

        var created = actionService.create(request);
        var cancelled = actionService.cancel(created.id);

        assertEquals(com.volunteer.actionservice.application.domain.ActionStatus.CANCELLED, cancelled.status);
    }

    @Test
    void listAllActions() {
        actionService.create(activism("Action 1", "Athens"));
        actionService.create(activism("Action 2", "Thessaloniki"));

        var list = actionService.listAll();

        assertEquals(5, list.size());
    }

    private ActionRequest activism(String title, String location) {
        ActionRequest request = new ActionRequest();
        request.type = ActionType.ACTIVISM;
        request.title = title;
        request.description = "Community activism";
        request.startDate = LocalDateTime.of(2026, 7, 1, 10, 0);
        request.endDate = LocalDateTime.of(2026, 7, 1, 14, 0);
        request.location = location;
        request.category = "environment";
        request.minParticipants = 5;
        request.maxParticipants = 25;
        return request;
    }
}
