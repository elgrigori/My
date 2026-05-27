package com.volunteer.userservice.adapters.in.rest;

import com.volunteer.userservice.adapters.in.rest.representation.LoginRequest;
import com.volunteer.userservice.application.ports.in.UserUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth")
@Tag(name = "Authentication")
public class AuthResource {
    @Inject
    UserUseCase userService;

    @POST
    @Path("/login")
    @Operation(summary = "Authenticate a registered user")
    public Response login(@Valid LoginRequest request) {
        return Response.ok(userService.authenticate(request)).build();
    }
}
