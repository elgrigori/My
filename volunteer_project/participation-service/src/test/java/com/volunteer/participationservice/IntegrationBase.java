package com.volunteer.participationservice;

import com.volunteer.participationservice.adapters.out.ParticipationRepositoryImpl;
import com.volunteer.participationservice.application.domain.Participation;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Base class for integration tests.
 * Resets the database before each test.
 */
public abstract class IntegrationBase {
    protected static final Long DESIGN_VOLUNTEER_ID = 21L;
    protected static final Long DESIGN_ACTIVISM_ACTION_ID = 100L;
    protected static final Long DESIGN_FUNDING_ACTION_ID = 101L;
    protected static final Long DESIGN_CONTRIBUTE_ACTION_ID = 102L;

    @Inject
    protected ParticipationRepositoryImpl participationRepository;

    @BeforeEach
    @Transactional
    protected void initDb() {
        participationRepository.deleteAll();
        participationRepository.save(designActivismParticipation());
        participationRepository.save(designFundingParticipation());
        participationRepository.save(designContributeParticipation());
    }

    private Participation designActivismParticipation() {
        Participation participation = participation(DESIGN_VOLUNTEER_ID, DESIGN_ACTIVISM_ACTION_ID,
                "ACTIVISM", ParticipationStatus.CONFIRMED,
                LocalDateTime.of(2026, 6, 10, 9, 0),
                LocalDateTime.of(2026, 6, 10, 13, 0));
        participation.notificationMessage = "Confirmation sent for action 100";
        participation.notificationHistory = participation.notificationMessage;
        return participation;
    }

    private Participation designFundingParticipation() {
        Participation participation = participation(DESIGN_VOLUNTEER_ID, DESIGN_FUNDING_ACTION_ID,
                "FUNDING", ParticipationStatus.CANCELLED,
                LocalDateTime.of(2026, 6, 1, 10, 0),
                LocalDateTime.of(2026, 6, 30, 18, 0));
        participation.amount = BigDecimal.valueOf(25.0);
        participation.notificationMessage = "Participation cancelled";
        participation.notificationHistory = "Confirmation sent for action 101\nParticipation cancelled";
        return participation;
    }

    private Participation designContributeParticipation() {
        Participation participation = participation(DESIGN_VOLUNTEER_ID, DESIGN_CONTRIBUTE_ACTION_ID,
                "CONTRIBUTE", ParticipationStatus.CANCELLED,
                LocalDateTime.of(2026, 6, 1, 10, 0),
                LocalDateTime.of(2026, 6, 15, 18, 0));
        participation.productsSummary = "1:5,2:3";
        participation.notificationMessage = "Participation cancelled";
        participation.notificationHistory = "Confirmation sent for action 102\nParticipation cancelled";
        return participation;
    }

    private Participation participation(Long volunteerId, Long actionId, String type, ParticipationStatus status,
                                        LocalDateTime startDate, LocalDateTime endDate) {
        Participation participation = new Participation();
        participation.volunteerId = volunteerId;
        participation.actionId = actionId;
        participation.type = type;
        participation.startDate = startDate;
        participation.endDate = endDate;
        participation.status = status;
        return participation;
    }
}
