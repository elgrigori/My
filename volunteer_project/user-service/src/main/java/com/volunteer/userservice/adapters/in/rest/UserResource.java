package com.volunteer.userservice.adapters.in.rest;

import com.volunteer.userservice.adapters.in.rest.representation.OrganizationRequest;
import com.volunteer.userservice.adapters.in.rest.representation.UserUpdateRequest;
import com.volunteer.userservice.adapters.in.rest.representation.VolunteerRequest;
import com.volunteer.userservice.application.ports.in.UserUseCase;
import jakarta.ws.rs.DELETE;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;

@Path("/")
@Tag(name = "Users")
public class UserResource {
    @Inject
    UserUseCase userService;

    @POST
    @Path("/organizations")
    @Operation(summary = "Register a new organization")
    public Response createOrganization(@Valid OrganizationRequest request) {
        var created = userService.createOrganization(request);
        return Response.created(URI.create("/organizations/" + created.id)).entity(created).build();
    }

    @GET
    @Path("/organizations")
    @Operation(summary = "List organizations")
    public Response listOrganizations() {
        return Response.ok(userService.listOrganizations()).build();
    }

    @GET
    @Path("/organizations/{id}")
    @Operation(summary = "Get an organization")
    public Response getOrganization(@PathParam("id") Long id) {
        return Response.ok(userService.getOrganization(id)).build();
    }

    @PUT
    @Path("/organizations/{id}")
    @Operation(summary = "Update an organization")
    public Response updateOrganization(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        return Response.ok(userService.updateOrganization(id, request)).build();
    }

    @DELETE
    @Path("/organizations/{id}")
    @Operation(summary = "Delete an organization")
    public Response deleteOrganization(@PathParam("id") Long id) {
        userService.deleteOrganization(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/organizations/{id}/exists")
    @Operation(summary = "Check whether an organization exists")
    public Response organizationExists(@PathParam("id") Long id) {
        return userService.organizationExists(id) ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/volunteers")
    @Operation(summary = "Register a new volunteer")
    public Response createVolunteer(@Valid VolunteerRequest request) {
        var created = userService.createVolunteer(request);
        return Response.created(URI.create("/volunteers/" + created.id)).entity(created).build();
    }

    @GET
    @Path("/volunteers")
    @Operation(summary = "List volunteers")
    public Response listVolunteers() {
        return Response.ok(userService.listVolunteers()).build();
    }

    @GET
    @Path("/volunteers/{id}")
    @Operation(summary = "Get a volunteer")
    public Response getVolunteer(@PathParam("id") Long id) {
        return Response.ok(userService.getVolunteer(id)).build();
    }

    @PUT
    @Path("/volunteers/{id}")
    @Operation(summary = "Update a volunteer")
    public Response updateVolunteer(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        return Response.ok(userService.updateVolunteer(id, request)).build();
    }

    @DELETE
    @Path("/volunteers/{id}")
    @Operation(summary = "Delete a volunteer")
    public Response deleteVolunteer(@PathParam("id") Long id) {
        userService.deleteVolunteer(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/volunteers/{id}/exists")
    @Operation(summary = "Check whether a volunteer exists")
    public Response volunteerExists(@PathParam("id") Long id) {
        return userService.volunteerExists(id) ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/users/{id}")
    @Operation(summary = "Get a user profile")
    public Response getUser(@PathParam("id") Long id) {
        return Response.ok(userService.getUser(id)).build();
    }

    @PUT
    @Path("/users/{id}")
    @Operation(summary = "Update a user profile")
    public Response updateUser(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        return Response.ok(userService.updateUser(id, request)).build();
    }
}
