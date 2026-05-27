package com.volunteer.participationservice.application.ports.in;

import com.volunteer.participationservice.adapters.in.rest.representation.ActionNotificationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationResponse;
import com.volunteer.participationservice.application.domain.ParticipationStatus;

import java.util.List;

public interface ParticipationUseCase {
    ParticipationResponse create(ParticipationRequest request);

    ParticipationResponse cancel(Long id);

    List<ParticipationResponse> findByVolunteer(Long volunteerId);

    List<ParticipationResponse> search(Long volunteerId, Long actionId, ParticipationStatus status);

    ParticipationResponse get(Long id);

    List<ParticipationResponse> findByAction(Long actionId);

    List<ParticipationResponse> notificationsForVolunteer(Long volunteerId);

    ParticipationResponse markNotificationRead(Long notificationId);

    void actionUpdated(ActionNotificationRequest request);

    void actionCancelled(ActionNotificationRequest request);
}
