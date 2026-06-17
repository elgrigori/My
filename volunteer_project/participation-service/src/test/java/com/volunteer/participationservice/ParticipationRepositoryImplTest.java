package com.volunteer.participationservice;

import com.volunteer.participationservice.application.domain.ParticipationStatus;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.volunteer.participationservice.fixture.ParticipationFixture.participation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class ParticipationRepositoryImplTest extends IntegrationBase {
    @Test
    @Transactional
    void searchAcceptedIncludesConfirmedParticipations() {
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        participationRepository.save(participation(301L, 401L, ParticipationStatus.CONFIRMED,
                start, start.plusHours(2)));

        var participations = participationRepository.search(301L, 401L, ParticipationStatus.ACCEPTED);

        assertEquals(1, participations.size());
        assertEquals(ParticipationStatus.CONFIRMED, participations.get(0).status);
    }

    @Test
    @Transactional
    void overlappingSearchIgnoresCancelledParticipations() {
        LocalDateTime start = LocalDateTime.now().plusDays(4);
        participationRepository.save(participation(302L, 402L, ParticipationStatus.CANCELLED,
                start, start.plusHours(2)));

        var overlaps = participationRepository.findOverlapping(302L, start.plusMinutes(30), start.plusHours(3));

        assertTrue(overlaps.isEmpty());
    }

    @Test
    @Transactional
    void findsAcceptedParticipationsByAction() {
        LocalDateTime start = LocalDateTime.now().plusDays(5);
        participationRepository.save(participation(303L, 403L, ParticipationStatus.CONFIRMED,
                start, start.plusHours(2)));
        participationRepository.save(participation(304L, 403L, ParticipationStatus.CANCELLED,
                start, start.plusHours(2)));

        var participations = participationRepository.findAcceptedByAction(403L);

        assertEquals(1, participations.size());
        assertEquals(303L, participations.get(0).volunteerId);
    }
}
