package com.volunteer.actionservice.adapters.in.rest;

import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FaultToleranceExceptionMapperTest {

    @Test
    void mapsTimeoutToServiceUnavailable() {
        try (var response = new FaultToleranceExceptionMapper()
                .toResponse(new TimeoutException("Downstream timeout"))) {
            assertEquals(503, response.getStatus());
            assertEquals("Downstream service unavailable",
                    ((ErrorResponse) response.getEntity()).message);
        }
    }
}
