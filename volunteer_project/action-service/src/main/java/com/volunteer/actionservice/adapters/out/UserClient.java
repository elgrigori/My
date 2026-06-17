package com.volunteer.actionservice.adapters.out;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "user-service")
public interface UserClient {
    @GET
    @Path("/organizations/{id}/exists")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    Response organizationExists(@PathParam("id") Long id);
}

