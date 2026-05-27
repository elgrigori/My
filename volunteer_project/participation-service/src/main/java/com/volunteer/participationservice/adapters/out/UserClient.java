package com.volunteer.participationservice.adapters.out;

import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
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
