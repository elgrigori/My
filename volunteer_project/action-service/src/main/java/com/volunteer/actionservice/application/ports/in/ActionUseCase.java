package com.volunteer.actionservice.application.ports.in;

import com.volunteer.actionservice.adapters.in.rest.representation.ActionRequest;
import com.volunteer.actionservice.adapters.in.rest.representation.ActionResponse;
import com.volunteer.actionservice.adapters.in.rest.representation.ParticipationUpdateRequest;
import com.volunteer.actionservice.application.domain.ActionStatus;
import com.volunteer.actionservice.application.domain.ActionType;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionUseCase {
    ActionResponse create(ActionRequest request);

    List<ActionResponse> search(String category, String location, String title, ActionType type,
                                ActionStatus status, LocalDateTime from, LocalDateTime to, Long organizationId);

    ActionResponse get(Long id);

    ActionResponse update(Long id, ActionRequest request);

    void delete(Long id);

    ActionResponse cancel(Long id);

    ActionResponse complete(Long id);

    ActionResponse availability(Long id);

    List<ActionResponse> findByOrganization(Long organizationId);

    ActionResponse participationAccepted(Long id, ParticipationUpdateRequest request);

    ActionResponse participationCancelled(Long id, ParticipationUpdateRequest request);
}
