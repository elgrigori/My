package com.volunteer.participationservice.adapters.out;

import com.volunteer.participationservice.adapters.in.rest.representation.UserSummary;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/volunteers")
@RegisterRestClient(configKey = "user-service")
public interface UserClient {
    @GET
    @Path("/{id}")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    UserSummary getUser(@PathParam("id") Long id);
}

