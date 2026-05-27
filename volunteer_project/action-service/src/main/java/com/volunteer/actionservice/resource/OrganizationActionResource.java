package com.volunteer.actionservice.resource;

import com.volunteer.actionservice.service.ActionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/organizations")
@Tag(name = "Organization actions")
public class OrganizationActionResource {
    @Inject
    ActionService actionService;

    @GET
    @Path("/{id}/actions")
    @Operation(summary = "List actions for an organization")
    public Response findByOrganization(@PathParam("id") Long organizationId) {
        return Response.ok(actionService.findByOrganization(organizationId)).build();
    }
}
