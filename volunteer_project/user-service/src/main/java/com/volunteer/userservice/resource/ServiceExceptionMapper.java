package com.volunteer.userservice.resource;

import com.volunteer.userservice.service.ServiceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {
    @Override
    public Response toResponse(ServiceException exception) {
        return Response.status(exception.status())
                .entity(new ErrorResponse(exception.getMessage()))
                .build();
    }
}
