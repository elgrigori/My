package com.volunteer.participationservice.application.ports.out;

import com.volunteer.participationservice.application.domain.Participation;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipationRepository extends PanacheRepository<Participation> {
    List<Participation> findConfirmedByVolunteer(Long volunteerId);

    List<Participation> findOverlapping(Long volunteerId, LocalDateTime startDate, LocalDateTime endDate);

    List<Participation> findByAction(Long actionId);

    List<Participation> findAcceptedByAction(Long actionId);

    List<Participation> findNotificationsForVolunteer(Long volunteerId);

    List<Participation> search(Long volunteerId, Long actionId, ParticipationStatus status);
}
