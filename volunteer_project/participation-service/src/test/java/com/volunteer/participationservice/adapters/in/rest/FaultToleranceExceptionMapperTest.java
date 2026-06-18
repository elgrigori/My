package com.volunteer.participationservice.adapters.in.rest;

import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FaultToleranceExceptionMapperTest {

    @Test
    void mapsOpenCircuitToServiceUnavailable() {
        try (var response = new FaultToleranceExceptionMapper()
                .toResponse(new CircuitBreakerOpenException("Circuit open"))) {
            assertEquals(503, response.getStatus());
            assertEquals("Downstream service unavailable",
                    ((ErrorResponse) response.getEntity()).message);
        }
    }
}
