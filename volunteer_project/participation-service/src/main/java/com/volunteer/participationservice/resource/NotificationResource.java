package com.volunteer.participationservice.resource;

import com.volunteer.participationservice.dto.ActionNotificationRequest;
import com.volunteer.participationservice.service.ParticipationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/notifications")
@Tag(name = "Notifications")
public class NotificationResource {
    @Inject
    ParticipationService participationService;

    @PATCH
    @Path("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public Response markNotificationRead(@PathParam("id") Long notificationId) {
        return Response.ok(participationService.markNotificationRead(notificationId)).build();
    }

    @POST
    @Path("/action-updated")
    @Operation(summary = "Create notifications after action update")
    public Response actionUpdated(ActionNotificationRequest request) {
        participationService.actionUpdated(request);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/action-cancelled")
    @Operation(summary = "Create notifications after action cancellation")
    public Response actionCancelled(ActionNotificationRequest request) {
        participationService.actionCancelled(request);
        return Response.status(Response.Status.CREATED).build();
    }
}
