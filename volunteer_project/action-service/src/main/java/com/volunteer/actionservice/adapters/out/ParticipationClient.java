package com.volunteer.actionservice.adapters.out;

import com.volunteer.actionservice.adapters.in.rest.representation.ActionNotificationRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/notifications")
@RegisterRestClient(configKey = "participation-service")
public interface ParticipationClient {
    @POST
    @Path("/action-updated")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    void actionUpdated(ActionNotificationRequest request);

    @POST
    @Path("/action-cancelled")
    @Timeout(1000)
    @Retry(maxRetries = 2, delay = 100)
    @CircuitBreaker(requestVolumeThreshold = 100, failureRatio = 0.5, delay = 5000)
    void actionCancelled(ActionNotificationRequest request);
}

