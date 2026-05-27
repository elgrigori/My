package com.volunteer.userservice.service;

import com.volunteer.userservice.dto.AuthResponse;
import com.volunteer.userservice.dto.LoginRequest;
import com.volunteer.userservice.dto.OrganizationRequest;
import com.volunteer.userservice.dto.UserResponse;
import com.volunteer.userservice.dto.UserUpdateRequest;
import com.volunteer.userservice.dto.VolunteerRequest;
import com.volunteer.userservice.entity.Organization;
import com.volunteer.userservice.entity.User;
import com.volunteer.userservice.entity.Volunteer;
import com.volunteer.userservice.repository.OrganizationRepository;
import com.volunteer.userservice.repository.UserRepository;
import com.volunteer.userservice.repository.VolunteerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    VolunteerRepository volunteerRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @ConfigProperty(name = "volunteer.password.min-length", defaultValue = "6")
    int minPasswordLength;

    @Transactional
    public UserResponse createVolunteer(VolunteerRequest request) {
        validateCredentials(request.username, request.email, request.password, null);
        Volunteer volunteer = new Volunteer();
        volunteer.username = request.username;
        volunteer.email = request.email;
        volunteer.password = request.password;
        volunteer.firstName = request.firstName;
        volunteer.lastName = request.lastName;
        volunteer.address = request.address;
        volunteer.city = request.city;
        volunteer.postalCode = request.postalCode;
        volunteer.phone = request.phone;
        volunteerRepository.persist(volunteer);
        return toResponse(volunteer);
    }

    @Transactional
    public UserResponse createOrganization(OrganizationRequest request) {
        validateCredentials(request.username, request.email, request.password, null);
        ensureAfmAvailable(request.afm, null);
        Organization organization = new Organization();
        organization.username = request.username;
        organization.email = request.email;
        organization.password = request.password;
        organization.afm = request.afm;
        organization.organizationName = request.organizationName;
        organization.description = request.description;
        organization.mission = request.mission;
        organization.foundedYear = request.foundedYear;
        organization.address = request.address;
        organization.city = request.city;
        organization.postalCode = request.postalCode;
        organization.phone = request.phone;
        organizationRepository.persist(organization);
        return toResponse(organization);
    }

    public UserResponse getUser(Long id) {
        return toResponse(findUser(id));
    }

    public List<UserResponse> listVolunteers() {
        return volunteerRepository.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<UserResponse> listOrganizations() {
        return organizationRepository.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getVolunteer(Long id) {
        return toResponse(findVolunteer(id));
    }

    public UserResponse getOrganization(Long id) {
        return toResponse(findOrganization(id));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findUser(id);
        return updateUser(user, request);
    }

    @Transactional
    public UserResponse updateVolunteer(Long id, UserUpdateRequest request) {
        return updateUser(findVolunteer(id), request);
    }

    @Transactional
    public UserResponse updateOrganization(Long id, UserUpdateRequest request) {
        return updateUser(findOrganization(id), request);
    }

    private UserResponse updateUser(User user, UserUpdateRequest request) {
        if (request.username != null && !Objects.equals(user.username, request.username)) {
            ensureUsernameAvailable(request.username, user.id);
            user.username = request.username;
        }
        if (request.email != null && !Objects.equals(user.email, request.email)) {
            ensureEmailAvailable(request.email, user.id);
            user.email = request.email;
        }
        if (request.password != null) {
            ensurePasswordLength(request.password);
            user.password = request.password;
        }
        if (request.address != null) user.address = request.address;
        if (request.city != null) user.city = request.city;
        if (request.postalCode != null) user.postalCode = request.postalCode;
        if (request.phone != null) user.phone = request.phone;

        if (user instanceof Volunteer volunteer) {
            if (request.firstName != null) volunteer.firstName = request.firstName;
            if (request.lastName != null) volunteer.lastName = request.lastName;
        }
        if (user instanceof Organization organization) {
            if (request.afm != null && !Objects.equals(organization.afm, request.afm)) {
                ensureAfmAvailable(request.afm, organization.id);
                organization.afm = request.afm;
            }
            if (request.organizationName != null) organization.organizationName = request.organizationName;
            if (request.description != null) organization.description = request.description;
            if (request.mission != null) organization.mission = request.mission;
            if (request.foundedYear != null) organization.foundedYear = request.foundedYear;
        }
        return toResponse(user);
    }

    @Transactional
    public void deleteVolunteer(Long id) {
        volunteerRepository.delete(findVolunteer(id));
    }

    @Transactional
    public void deleteOrganization(Long id) {
        organizationRepository.delete(findOrganization(id));
    }

    public boolean volunteerExists(Long id) {
        return volunteerRepository.findByIdOptional(id).isPresent();
    }

    public boolean organizationExists(Long id) {
        return organizationRepository.findByIdOptional(id).isPresent();
    }

    public AuthResponse authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.username)
                .orElseThrow(() -> new ServiceException(Response.Status.UNAUTHORIZED, "Invalid username or password"));
        if (!Objects.equals(user.password, request.password)) {
            throw new ServiceException(Response.Status.UNAUTHORIZED, "Invalid username or password");
        }
        AuthResponse response = new AuthResponse();
        response.userId = user.id;
        response.username = user.username;
        response.type = user.type();
        response.token = Base64.getEncoder().encodeToString((user.username + ":" + user.type()).getBytes(StandardCharsets.UTF_8));
        return response;
    }

    private User findUser(Long id) {
        return userRepository.findByIdOptional(id)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "User not found"));
    }

    private Volunteer findVolunteer(Long id) {
        return volunteerRepository.findByIdOptional(id)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "Volunteer not found"));
    }

    private Organization findOrganization(Long id) {
        return organizationRepository.findByIdOptional(id)
                .orElseThrow(() -> new ServiceException(Response.Status.NOT_FOUND, "Organization not found"));
    }

    private void validateCredentials(String username, String email, String password, Long currentId) {
        ensurePasswordLength(password);
        ensureUsernameAvailable(username, currentId);
        ensureEmailAvailable(email, currentId);
    }

    private void ensurePasswordLength(String password) {
        if (password == null || password.length() < minPasswordLength) {
            throw new ServiceException(Response.Status.BAD_REQUEST, "Password must contain at least " + minPasswordLength + " characters");
        }
    }

    private void ensureUsernameAvailable(String username, Long currentId) {
        userRepository.findByUsername(username)
                .filter(existing -> !Objects.equals(existing.id, currentId))
                .ifPresent(existing -> {
                    throw new ServiceException(Response.Status.CONFLICT, "Username already exists");
                });
    }

    private void ensureEmailAvailable(String email, Long currentId) {
        userRepository.findByEmail(email)
                .filter(existing -> !Objects.equals(existing.id, currentId))
                .ifPresent(existing -> {
                    throw new ServiceException(Response.Status.CONFLICT, "Email already exists");
                });
    }

    private void ensureAfmAvailable(String afm, Long currentId) {
        organizationRepository.findByAfm(afm)
                .filter(existing -> !Objects.equals(existing.id, currentId))
                .ifPresent(existing -> {
                    throw new ServiceException(Response.Status.CONFLICT, "AFM already exists");
                });
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.id = user.id;
        response.type = user.type();
        response.username = user.username;
        response.email = user.email;
        response.address = user.address;
        response.city = user.city;
        response.postalCode = user.postalCode;
        response.phone = user.phone;
        if (user instanceof Volunteer volunteer) {
            response.firstName = volunteer.firstName;
            response.lastName = volunteer.lastName;
        }
        if (user instanceof Organization organization) {
            response.afm = organization.afm;
            response.organizationName = organization.organizationName;
            response.description = organization.description;
            response.mission = organization.mission;
            response.foundedYear = organization.foundedYear;
        }
        return response;
    }
}
