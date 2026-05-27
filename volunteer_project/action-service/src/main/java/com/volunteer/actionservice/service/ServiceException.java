package com.volunteer.actionservice.service;

import jakarta.ws.rs.core.Response;

public class ServiceException extends RuntimeException {
    private final Response.Status status;

    public ServiceException(Response.Status status, String message) {
        super(message);
        this.status = status;
    }

    public Response.Status status() {
        return status;
    }
}
