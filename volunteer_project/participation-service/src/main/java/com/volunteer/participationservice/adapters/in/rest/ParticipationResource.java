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
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path(ApiPath.ROOT.PARTICIPATIONS)
@Tag(name = "Participations")
public class ParticipationResource {
    @Inject
    ParticipationUseCase participationService;

    @POST
    @Operation(summary = "Register a volunteer participation")
    @Counted(name = "participations_created_total", absolute = true, description = "Total participation creation requests")
    @Timed(name = "participations_create_seconds", absolute = true, description = "Participation creation latency")
    public Response create(@Valid ParticipationRequest request) {
        var created = participationService.create(request);
        return Response.created(URI.create(ApiPath.ROOT.PARTICIPATIONS + "/" + created.id)).entity(created).build();
    }

    @DELETE
    @Path("/{id:\\d+}")
    @Operation(summary = "Cancel a participation")
    public Response cancel(@PathParam("id") Long id) {
        participationService.cancel(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id:\\d+}/cancel")
    @Operation(summary = "Cancel a participation")
    @Counted(name = "participations_cancelled_total", absolute = true, description = "Total participation cancellation requests")
    @Timed(name = "participations_cancel_seconds", absolute = true, description = "Participation cancellation latency")
    public Response cancelWithPatch(@PathParam("id") Long id) {
        return Response.ok(participationService.cancel(id)).build();
    }

    @GET
    @Operation(summary = "List participations")
    @Counted(name = "participations_searched_total", absolute = true, description = "Total participation search requests")
    @Timed(name = "participations_search_seconds", absolute = true, description = "Participation search latency")
    public Response search(@QueryParam("volunteerId") Long volunteerId,
                           @QueryParam("actionId") Long actionId,
                           @QueryParam("status") ParticipationStatus status) {
        return Response.ok(participationService.search(volunteerId, actionId, status)).build();
    }

    @GET
    @Path("/{id:\\d+}")
    @Operation(summary = "Get a participation")
    @Counted(name = "participations_read_total", absolute = true, description = "Total participation read requests")
    @Timed(name = "participations_read_seconds", absolute = true, description = "Participation read latency")
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

