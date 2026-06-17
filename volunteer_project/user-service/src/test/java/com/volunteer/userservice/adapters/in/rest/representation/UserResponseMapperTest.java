package com.volunteer.userservice.adapters.in.rest.representation;

import com.volunteer.userservice.application.UserService;
import org.junit.jupiter.api.Test;

import static com.volunteer.userservice.fixture.UserFixture.organization;
import static com.volunteer.userservice.fixture.UserFixture.volunteer;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserResponseMapperTest {
    private final UserService userService = new UserService();

    @Test
    void mapsVolunteerFields() {
        var response = userService.toResponse(volunteer("ada", "ada@example.com"));

        assertEquals("VOLUNTEER", response.type);
        assertEquals("ada", response.username);
        assertEquals("ada@example.com", response.email);
        assertEquals("Ada", response.firstName);
        assertEquals("Lovelace", response.lastName);
    }

    @Test
    void mapsOrganizationFields() {
        var response = userService.toResponse(organization("help-org-test", "help@example.com", "987654321"));

        assertEquals("ORGANIZATION", response.type);
        assertEquals("help-org-test", response.username);
        assertEquals("987654321", response.afm);
        assertEquals("Helping Hands", response.organizationName);
        assertEquals("Community support", response.description);
    }
}
