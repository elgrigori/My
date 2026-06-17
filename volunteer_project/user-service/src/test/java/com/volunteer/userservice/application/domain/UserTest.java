package com.volunteer.userservice.application.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void volunteerTypeIsVolunteer() {
        assertEquals("VOLUNTEER", new Volunteer().type());
    }

    @Test
    void organizationTypeIsOrganization() {
        assertEquals("ORGANIZATION", new Organization().type());
    }
}
