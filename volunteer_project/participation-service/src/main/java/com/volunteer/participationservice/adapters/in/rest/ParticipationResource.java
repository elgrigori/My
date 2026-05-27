package com.volunteer.participationservice.adapters.in.rest;

import com.volunteer.participationservice.adapters.in.rest.representation.ParticipationRequest;
import com.volunteer.participationservice.application.domain.ParticipationStatus;
import com.volunteer.participationservice.application.ports.in.ParticipationUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/participations")
@Tag(name = "Participations")
public class ParticipationResource {
    @Inject
    ParticipationUseCase participationService;

    @POST
    @Operation(summary = "Register a volunteer participation")
    public Response create(@Valid ParticipationRequest request) {
        var created = participationService.create(request);
        return Response.created(URI.create("/participations/" + created.id)).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Cancel a participation")
    public Response cancel(@PathParam("id") Long id) {
        participationService.cancel(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/cancel")
    @Operation(summary = "Cancel a participation")
    public Response cancelWithPatch(@PathParam("id") Long id) {
        return Response.ok(participationService.cancel(id)).build();
    }

    @GET
    @Operation(summary = "List participations")
    public Response search(@QueryParam("volunteerId") Long volunteerId,
                           @QueryParam("actionId") Long actionId,
                           @QueryParam("status") ParticipationStatus status) {
        return Response.ok(participationService.search(volunteerId, actionId, status)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a participation")
    public Response get(@PathParam("id") Long id) {
        return Response.ok(participationService.get(id)).build();
    }

    @GET
    @Path("/volunteer/{id}")
    @Operation(summary = "List confirmed participations for a volunteer")
    public Response findByVolunteer(@PathParam("id") Long volunteerId) {
        return Response.ok(participationService.findByVolunteer(volunteerId)).build();
    }
}
