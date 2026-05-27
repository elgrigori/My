package com.volunteer.actionservice.adapters.in.rest;

import com.volunteer.actionservice.adapters.in.rest.representation.ActionRequest;
import com.volunteer.actionservice.adapters.in.rest.representation.ParticipationUpdateRequest;
import com.volunteer.actionservice.application.domain.ActionStatus;
import com.volunteer.actionservice.application.domain.ActionType;
import com.volunteer.actionservice.application.ports.in.ActionUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.time.LocalDateTime;

@Path("/actions")
@Tag(name = "Actions")
public class ActionResource {
    @Inject
    ActionUseCase actionService;

    @POST
    @Operation(summary = "Create a volunteer action")
    public Response create(@Valid ActionRequest request) {
        var created = actionService.create(request);
        return Response.created(URI.create("/actions/" + created.id)).entity(created).build();
    }

    @GET
    @Operation(summary = "Search volunteer actions")
    public Response search(@QueryParam("category") String category,
                           @QueryParam("location") String location,
                           @QueryParam("city") String city,
                           @QueryParam("title") String title,
                           @QueryParam("type") ActionType type,
                           @QueryParam("status") ActionStatus status,
                           @QueryParam("from") LocalDateTime from,
                           @QueryParam("to") LocalDateTime to,
                           @QueryParam("organizationId") Long organizationId) {
        String locationFilter = location != null ? location : city;
        return Response.ok(actionService.search(category, locationFilter, title, type, status, from, to, organizationId)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a volunteer action")
    public Response get(@PathParam("id") Long id) {
        return Response.ok(actionService.get(id)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a volunteer action")
    public Response update(@PathParam("id") Long id, @Valid ActionRequest request) {
        return Response.ok(actionService.update(id, request)).build();
    }

    @PATCH
    @Path("/{id}/cancel")
    @Operation(summary = "Cancel a volunteer action")
    public Response cancel(@PathParam("id") Long id) {
        return Response.ok(actionService.cancel(id)).build();
    }

    @PATCH
    @Path("/{id}/complete")
    @Operation(summary = "Complete a volunteer action")
    public Response complete(@PathParam("id") Long id) {
        return Response.ok(actionService.complete(id)).build();
    }

    @GET
    @Path("/{id}/availability")
    @Operation(summary = "Check action availability")
    public Response availability(@PathParam("id") Long id) {
        return Response.ok(actionService.availability(id)).build();
    }

    @PATCH
    @Path("/{id}/participation-accepted")
    @Operation(summary = "Update action counters after accepted participation")
    public Response participationAccepted(@PathParam("id") Long id, ParticipationUpdateRequest request) {
        return Response.ok(actionService.participationAccepted(id, request)).build();
    }

    @PATCH
    @Path("/{id}/participation-cancelled")
    @Operation(summary = "Update action counters after cancelled participation")
    public Response participationCancelled(@PathParam("id") Long id, ParticipationUpdateRequest request) {
        return Response.ok(actionService.participationCancelled(id, request)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a volunteer action")
    public Response delete(@PathParam("id") Long id) {
        actionService.delete(id);
        return Response.noContent().build();
    }
}
