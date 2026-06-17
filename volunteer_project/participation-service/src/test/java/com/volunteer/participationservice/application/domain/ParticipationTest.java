package com.volunteer.participationservice.application.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParticipationTest {

    @Test
    void representsActivismParticipation() {
        LocalDateTime start = LocalDateTime.now().plusDays(3);

        Participation participation = new Participation();
        participation.volunteerId = 10L;
        participation.actionId = 20L;
        participation.type = "ACTIVISM";
        participation.startDate = start;
        participation.endDate = start.plusHours(2);
        participation.status = ParticipationStatus.CONFIRMED;

        assertEquals(10L, participation.volunteerId);
        assertEquals(20L, participation.actionId);
        assertEquals("ACTIVISM", participation.type);
        assertEquals(ParticipationStatus.CONFIRMED, participation.status);
    }

    @Test
    void representsFundingParticipation() {
        Participation participation = new Participation();
        participation.type = "FUNDING";
        participation.amount = BigDecimal.valueOf(25);

        assertEquals("FUNDING", participation.type);
        assertEquals(BigDecimal.valueOf(25), participation.amount);
    }

    @Test
    void representsContributeParticipation() {
        Participation participation = new Participation();
        participation.type = "CONTRIBUTE";
        participation.productsSummary = "Rice:5,Milk:3";

        assertEquals("CONTRIBUTE", participation.type);
        assertEquals("Rice:5,Milk:3", participation.productsSummary);
    }

    @Test
    void storesNotificationState() {
        Participation participation = new Participation();
        participation.notificationMessage = "Action updated";
        participation.notificationHistory = "Participation confirmed\nAction updated";
        participation.notificationRead = false;

        assertEquals("Action updated", participation.notificationMessage);
        assertEquals("Participation confirmed\nAction updated", participation.notificationHistory);
        assertFalse(participation.notificationRead);

        participation.notificationRead = true;

        assertTrue(participation.notificationRead);
    }

    @Test
    void canBeMarkedCancelled() {
        Participation participation = new Participation();
        participation.status = ParticipationStatus.CONFIRMED;

        participation.status = ParticipationStatus.CANCELLED;

        assertEquals(ParticipationStatus.CANCELLED, participation.status);
    }
}
