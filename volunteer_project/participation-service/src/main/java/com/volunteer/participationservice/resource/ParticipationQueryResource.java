package com.volunteer.participationservice.resource;

import com.volunteer.participationservice.service.ParticipationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/")
@Tag(name = "Participation queries")
public class ParticipationQueryResource {
    @Inject
    ParticipationService participationService;

    @GET
    @Path("/volunteers/{id}/participations")
    @Operation(summary = "List participations for a volunteer")
    public Response findByVolunteer(@PathParam("id") Long volunteerId) {
        return Response.ok(participationService.findByVolunteer(volunteerId)).build();
    }

    @GET
    @Path("/actions/{id}/participations")
    @Operation(summary = "List participations for an action")
    public Response findByAction(@PathParam("id") Long actionId) {
        return Response.ok(participationService.findByAction(actionId)).build();
    }

    @GET
    @Path("/volunteers/{id}/notifications")
    @Operation(summary = "List volunteer notifications")
    public Response notificationsForVolunteer(@PathParam("id") Long volunteerId) {
        return Response.ok(participationService.notificationsForVolunteer(volunteerId)).build();
    }
}
