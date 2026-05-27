package com.volunteer.actionservice.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "user-service")
public interface UserClient {
    @GET
    @Path("/organizations/{id}/exists")
    Response organizationExists(@PathParam("id") Long id);
}
