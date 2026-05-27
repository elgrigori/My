package com.volunteer.participationservice.repository;

import com.volunteer.participationservice.entity.Participation;
import com.volunteer.participationservice.entity.ParticipationStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ParticipationRepository implements PanacheRepository<Participation> {
    public List<Participation> findConfirmedByVolunteer(Long volunteerId) {
        return list("volunteerId = ?1 and status in ?2", volunteerId,
                List.of(ParticipationStatus.CONFIRMED, ParticipationStatus.ACCEPTED));
    }

    public List<Participation> findOverlapping(Long volunteerId, LocalDateTime startDate, LocalDateTime endDate) {
        return list("volunteerId = ?1 and status in ?2 and startDate < ?3 and endDate > ?4",
                volunteerId, List.of(ParticipationStatus.CONFIRMED, ParticipationStatus.ACCEPTED), endDate, startDate);
    }

    public List<Participation> findByAction(Long actionId) {
        return list("actionId", actionId);
    }

    public List<Participation> findAcceptedByAction(Long actionId) {
        return list("actionId = ?1 and status in ?2", actionId,
                List.of(ParticipationStatus.CONFIRMED, ParticipationStatus.ACCEPTED));
    }

    public List<Participation> search(Long volunteerId, Long actionId, ParticipationStatus status) {
        if (volunteerId != null && actionId != null && status != null) {
            return list("volunteerId = ?1 and actionId = ?2 and status in ?3", volunteerId, actionId, normalize(status));
        }
        if (volunteerId != null && actionId != null) {
            return list("volunteerId = ?1 and actionId = ?2", volunteerId, actionId);
        }
        if (volunteerId != null && status != null) {
            return list("volunteerId = ?1 and status in ?2", volunteerId, normalize(status));
        }
        if (actionId != null && status != null) {
            return list("actionId = ?1 and status in ?2", actionId, normalize(status));
        }
        if (volunteerId != null) {
            return list("volunteerId", volunteerId);
        }
        if (actionId != null) {
            return list("actionId", actionId);
        }
        if (status != null) {
            return list("status in ?1", normalize(status));
        }
        return listAll();
    }

    private List<ParticipationStatus> normalize(ParticipationStatus status) {
        return status == ParticipationStatus.ACCEPTED
                ? List.of(ParticipationStatus.ACCEPTED, ParticipationStatus.CONFIRMED)
                : List.of(status);
    }
}
