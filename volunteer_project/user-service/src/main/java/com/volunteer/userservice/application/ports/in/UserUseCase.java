package com.volunteer.userservice.application.ports.in;

import com.volunteer.userservice.adapters.in.rest.representation.OrganizationRequest;
import com.volunteer.userservice.adapters.in.rest.representation.AuthResponse;
import com.volunteer.userservice.adapters.in.rest.representation.LoginRequest;
import com.volunteer.userservice.adapters.in.rest.representation.UserResponse;
import com.volunteer.userservice.adapters.in.rest.representation.UserUpdateRequest;
import com.volunteer.userservice.adapters.in.rest.representation.VolunteerRequest;

import java.util.List;

public interface UserUseCase {
    UserResponse createVolunteer(VolunteerRequest request);

    UserResponse createOrganization(OrganizationRequest request);

    AuthResponse authenticate(LoginRequest request);

    List<UserResponse> listVolunteers();

    List<UserResponse> listOrganizations();

    UserResponse getVolunteer(Long id);

    UserResponse getOrganization(Long id);

    UserResponse updateVolunteer(Long id, UserUpdateRequest request);

    UserResponse updateOrganization(Long id, UserUpdateRequest request);

    void deleteVolunteer(Long id);

    void deleteOrganization(Long id);

    boolean volunteerExists(Long id);

    boolean organizationExists(Long id);
}
