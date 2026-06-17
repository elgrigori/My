package com.volunteer.userservice.adapters.in.rest;

import com.volunteer.userservice.adapters.in.rest.representation.OrganizationRequest;
import com.volunteer.userservice.adapters.in.rest.representation.UserUpdateRequest;
import com.volunteer.userservice.adapters.in.rest.representation.VolunteerRequest;
import com.volunteer.userservice.application.ports.in.UserUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
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
    @Counted(name = "organizations_created_total", absolute = true, description = "Total organization creation requests")
    @Timed(name = "organizations_create_seconds", absolute = true, description = "Organization creation latency")
    public Response createOrganization(@Valid OrganizationRequest request) {
        var created = userService.createOrganization(request);
        return Response.created(URI.create("/organizations/" + created.id)).entity(created).build();
    }

    @GET
    @Path("/organizations")
    @Counted(name = "organizations_listed_total", absolute = true, description = "Total organization listing requests")
    @Timed(name = "organizations_list_seconds", absolute = true, description = "Organization listing latency")
    public Response listOrganizations() {
        return Response.ok(userService.listOrganizations()).build();
    }

    @GET
    @Path("/organizations/{id}")
    @Counted(name = "organizations_read_total", absolute = true, description = "Total organization read requests")
    @Timed(name = "organizations_read_seconds", absolute = true, description = "Organization read latency")
    public Response getOrganization(@PathParam("id") Long id) {
        return Response.ok(userService.getOrganization(id)).build();
    }

    @PUT
    @Path("/organizations/{id}")
    public Response updateOrganization(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        return Response.ok(userService.updateOrganization(id, request)).build();
    }

    @DELETE
    @Path("/organizations/{id}")
    public Response deleteOrganization(@PathParam("id") Long id) {
        userService.deleteOrganization(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/organizations/{id}/exists")
    public Response organizationExists(@PathParam("id") Long id) {
        return userService.organizationExists(id) ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/volunteers")
    @Counted(name = "volunteers_created_total", absolute = true, description = "Total volunteer creation requests")
    @Timed(name = "volunteers_create_seconds", absolute = true, description = "Volunteer creation latency")
    public Response createVolunteer(@Valid VolunteerRequest request) {
        var created = userService.createVolunteer(request);
        return Response.created(URI.create("/volunteers/" + created.id)).entity(created).build();
    }

    @GET
    @Path("/volunteers")
    @Counted(name = "volunteers_listed_total", absolute = true, description = "Total volunteer listing requests")
    @Timed(name = "volunteers_list_seconds", absolute = true, description = "Volunteer listing latency")
    public Response listVolunteers() {
        return Response.ok(userService.listVolunteers()).build();
    }

    @GET
    @Path("/volunteers/{id}")
    @Counted(name = "volunteers_read_total", absolute = true, description = "Total volunteer read requests")
    @Timed(name = "volunteers_read_seconds", absolute = true, description = "Volunteer read latency")
    public Response getVolunteer(@PathParam("id") Long id) {
        return Response.ok(userService.getVolunteer(id)).build();
    }

    @PUT
    @Path("/volunteers/{id}")
    public Response updateVolunteer(@PathParam("id") Long id, @Valid UserUpdateRequest request) {
        return Response.ok(userService.updateVolunteer(id, request)).build();
    }

    @DELETE
    @Path("/volunteers/{id}")
    public Response deleteVolunteer(@PathParam("id") Long id) {
        userService.deleteVolunteer(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/volunteers/{id}/exists")
    public Response volunteerExists(@PathParam("id") Long id) {
        return userService.volunteerExists(id) ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}

