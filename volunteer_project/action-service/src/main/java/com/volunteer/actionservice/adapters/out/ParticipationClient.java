package com.volunteer.actionservice.adapters.out;

import com.volunteer.actionservice.adapters.in.rest.representation.ActionNotificationRequest;
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
