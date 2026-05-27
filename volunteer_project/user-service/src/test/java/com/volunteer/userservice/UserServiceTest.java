package com.volunteer.userservice;

import com.volunteer.userservice.dto.OrganizationRequest;
import com.volunteer.userservice.dto.VolunteerRequest;
import com.volunteer.userservice.repository.UserRepository;
import com.volunteer.userservice.service.ServiceException;
import com.volunteer.userservice.service.UserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class UserServiceTest {
    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void createsOrganizationWithUniqueAfm() {
        var request = organization("org-user", "org@example.com", "123456789");

        var response = userService.createOrganization(request);

        assertEquals("ORGANIZATION", response.type);
        assertEquals("Helping Hands", response.organizationName);
    }

    @Test
    void rejectsDuplicateUsername() {
        userService.createVolunteer(volunteer("shared", "first@example.com"));

        var duplicate = volunteer("shared", "second@example.com");
        var exception = assertThrows(ServiceException.class, () -> userService.createVolunteer(duplicate));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void rejectsDuplicateEmail() {
        userService.createVolunteer(volunteer("user-a", "same@example.com"));

        var duplicate = volunteer("user-b", "same@example.com");
        var exception = assertThrows(ServiceException.class, () -> userService.createVolunteer(duplicate));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void rejectsShortPassword() {
        var request = volunteer("shortpass", "shortpass@example.com");
        request.password = "123";

        assertThrows(ServiceException.class, () -> userService.createVolunteer(request));
    }

    @Test
    void deletesVolunteer() {
        var created = userService.createVolunteer(volunteer("todelete", "todelete@example.com"));

        userService.deleteVolunteer(created.id);

        assertThrows(ServiceException.class, () -> userService.getVolunteer(created.id));
    }

    private VolunteerRequest volunteer(String username, String email) {
        VolunteerRequest request = new VolunteerRequest();
        request.username = username;
        request.email = email;
        request.password = "secret1";
        request.firstName = "Ada";
        request.lastName = "Lovelace";
        request.city = "Athens";
        return request;
    }

    private OrganizationRequest organization(String username, String email, String afm) {
        OrganizationRequest request = new OrganizationRequest();
        request.username = username;
        request.email = email;
        request.password = "secret1";
        request.afm = afm;
        request.organizationName = "Helping Hands";
        request.description = "Community support";
        request.mission = "Coordinate volunteer work";
        request.foundedYear = 2015;
        request.address = "1 Solidarity St";
        request.city = "Athens";
        request.postalCode = "10431";
        request.phone = "+302101234567";
        return request;
    }
}
