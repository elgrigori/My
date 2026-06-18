package com.volunteer.actionservice.adapters.in.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.faulttolerance.exceptions.FaultToleranceException;

@Provider
public class FaultToleranceExceptionMapper implements ExceptionMapper<FaultToleranceException> {

    @Override
    public Response toResponse(FaultToleranceException exception) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(new ErrorResponse("Downstream service unavailable"))
                .build();
    }
}
