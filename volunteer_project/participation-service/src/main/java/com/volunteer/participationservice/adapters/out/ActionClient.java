package com.volunteer.participationservice.adapters.out;

import com.volunteer.participationservice.adapters.in.rest.representation.ActionCounterRequest;
import com.volunteer.participationservice.adapters.in.rest.representation.ActionSummary;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/actions")
@RegisterRestClient(configKey = "action-service")
public interface ActionClient {
    @GET
    @Path("/{id}")
    ActionSummary getAction(@PathParam("id") Long id);

    @GET
    @Path("/{id}/availability")
    ActionSummary getAvailability(@PathParam("id") Long id);

    @PATCH
    @Path("/{id}/participation-accepted")
    ActionSummary participationAccepted(@PathParam("id") Long id, ActionCounterRequest request);

    @PATCH
    @Path("/{id}/participation-cancelled")
    ActionSummary participationCancelled(@PathParam("id") Long id, ActionCounterRequest request);
}
