package com.volunteer.userservice.common;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Provider
@Priority(Priorities.USER)
public class FailureSimulationFilter implements ContainerRequestFilter {
    @ConfigProperty(name = "volunteer.simulation.delay-ms", defaultValue = "0")
    long delayMs;

    @ConfigProperty(name = "volunteer.simulation.failure-rate", defaultValue = "0")
    double failureRate;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path.startsWith("q/")) {
            return;
        }
        delayIfConfigured();
        failIfConfigured();
    }

    private void delayIfConfigured() {
        if (delayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ServiceUnavailableException("Simulated delay was interrupted");
        }
    }

    private void failIfConfigured() {
        if (failureRate <= 0) {
            return;
        }
        double boundedRate = Math.min(1, failureRate);
        if (ThreadLocalRandom.current().nextDouble() < boundedRate) {
            throw new ServiceUnavailableException("Simulated service failure");
        }
    }
}
