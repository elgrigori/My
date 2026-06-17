package com.volunteer.participationservice.adapters.out;

import com.volunteer.participationservice.adapters.in.rest.representation.ActionCounterRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/actions")
@RegisterRestClient(configKey = "action-service")
public interface ActionClient {
    @GET
    @Path("/{id}")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    ActionSummary getAction(@PathParam("id") Long id);

    @GET
    @Path("/{id}/availability")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    ActionSummary getAvailability(@PathParam("id") Long id);

    @PATCH
    @Path("/{id}/participation-accepted")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    ActionSummary participationAccepted(@PathParam("id") Long id, ActionCounterRequest request);

    @PATCH
    @Path("/{id}/participation-cancelled")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    ActionSummary participationCancelled(@PathParam("id") Long id, ActionCounterRequest request);
}

