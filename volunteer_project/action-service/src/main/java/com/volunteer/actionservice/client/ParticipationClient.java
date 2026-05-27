package com.volunteer.actionservice.client;

import com.volunteer.actionservice.dto.ActionNotificationRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/notifications")
@RegisterRestClient(configKey = "participation-service")
public interface ParticipationClient {
    @POST
    @Path("/action-updated")
    void actionUpdated(ActionNotificationRequest request);

    @POST
    @Path("/action-cancelled")
    void actionCancelled(ActionNotificationRequest request);
}
