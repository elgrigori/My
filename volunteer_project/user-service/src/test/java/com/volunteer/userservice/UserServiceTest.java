package com.volunteer.userservice;

import com.volunteer.userservice.adapters.in.rest.representation.OrganizationRequest;
import com.volunteer.userservice.adapters.in.rest.representation.VolunteerRequest;
import com.volunteer.userservice.application.ServiceException;
import com.volunteer.userservice.application.UserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class UserServiceTest extends IntegrationBase {
    @Inject
    UserService userService;

    @Test
    void createsOrganizationWithUniqueAfm() {
        var request = organization("org-user", "org@example.com", "987654321");

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
    void rejectsDuplicateOrganizationEmail() {
        userService.createOrganization(organization("org-a", "same@example.com", "111222333"));

        var duplicate = organization("org-b", "same@example.com", "444555666");
        var exception = assertThrows(ServiceException.class, () -> userService.createOrganization(duplicate));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void organizationExistsReturnsTrueAndFalse() {
        var created = userService.createOrganization(organization("org-exists", "org-exists@example.com", "777888999"));

        assertEquals(true, userService.organizationExists(created.id));
        assertEquals(false, userService.organizationExists(99999L));
    }

    @Test
    void volunteerExistsReturnsTrueAndFalse() {
        var created = userService.createVolunteer(volunteer("exists-vol", "exists-vol@example.com"));

        assertEquals(true, userService.volunteerExists(created.id));
        assertEquals(false, userService.volunteerExists(99999L));
    }

    @Test
    void deletesVolunteer() {
        var created = userService.createVolunteer(volunteer("todelete", "todelete@example.com"));

        userService.deleteVolunteer(created.id);

        assertThrows(ServiceException.class, () -> userService.getVolunteer(created.id));
    }

    @Test
    void getsVolunteerById() {
        var created = userService.createVolunteer(volunteer("getvolunteer", "getvolunteer@example.com"));

        var retrieved = userService.getVolunteer(created.id);

        assertEquals(created.id, retrieved.id);
        assertEquals("getvolunteer", retrieved.username);
    }

    @Test
    void getsOrganizationById() {
        var created = userService.createOrganization(organization("getorg", "getorg@example.com", "555666777"));

        var retrieved = userService.getOrganization(created.id);

        assertEquals(created.id, retrieved.id);
        assertEquals("getorg", retrieved.username);
    }

    @Test
    void listVolunteers() {
        userService.createVolunteer(volunteer("volunteer1", "vol1@example.com"));
        userService.createVolunteer(volunteer("volunteer2", "vol2@example.com"));

        var list = userService.listVolunteers();

        assertEquals(3, list.size());
    }

    @Test
    void listOrganizations() {
        userService.createOrganization(organization("org1", "org1@example.com", "999888777"));
        userService.createOrganization(organization("org2", "org2@example.com", "666555444"));

        var list = userService.listOrganizations();

        assertEquals(3, list.size());
    }

    @Test
    void rejectsDuplicateAfmInOrganization() {
        var duplicate = organization("org-b", "orgb@example.com", "123456789");
        var exception = assertThrows(ServiceException.class, () -> userService.createOrganization(duplicate));

        assertEquals("AFM already exists", exception.getMessage());
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
