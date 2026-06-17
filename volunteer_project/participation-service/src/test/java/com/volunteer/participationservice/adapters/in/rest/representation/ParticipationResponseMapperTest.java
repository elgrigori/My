package com.volunteer.participationservice.adapters.in.rest.representation;

import com.volunteer.participationservice.application.ParticipationService;
import com.volunteer.participationservice.application.domain.Participation;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ParticipationResponseMapperTest {

    @Test
    void toResponseMapsParticipationFields() {
        LocalDateTime start = LocalDateTime.now().plusDays(6);
        Participation participation = new Participation();
        participation.id = 1L;
        participation.type = "FUNDING";
        participation.volunteerId = 10L;
        participation.actionId = 20L;
        participation.amount = BigDecimal.valueOf(50);
        participation.productsSummary = "Rice:5";
        participation.startDate = start;
        participation.endDate = start.plusHours(2);
        participation.status = ParticipationStatus.CONFIRMED;
        participation.notificationMessage = "Participation confirmed";
        participation.notificationRead = false;

        ParticipationResponse response = new ParticipationService().toResponse(participation);

        assertEquals(participation.id, response.id);
        assertEquals(participation.type, response.type);
        assertEquals(participation.volunteerId, response.volunteerId);
        assertEquals(participation.actionId, response.actionId);
        assertEquals(participation.amount, response.amount);
        assertEquals(participation.productsSummary, response.productsSummary);
        assertEquals(participation.startDate, response.startDate);
        assertEquals(participation.endDate, response.endDate);
        assertEquals(participation.status, response.status);
        assertEquals(participation.notificationMessage, response.notificationMessage);
        assertFalse(response.notificationRead);
    }
}
