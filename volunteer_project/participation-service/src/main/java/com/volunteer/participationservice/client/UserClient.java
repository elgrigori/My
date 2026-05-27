package com.volunteer.participationservice.client;

import com.volunteer.participationservice.dto.UserSummary;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/users")
@RegisterRestClient(configKey = "user-service")
public interface UserClient {
    @GET
    @Path("/{id}")
    UserSummary getUser(@PathParam("id") Long id);
}
