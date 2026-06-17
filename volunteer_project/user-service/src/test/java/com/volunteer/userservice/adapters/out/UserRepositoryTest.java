package com.volunteer.userservice.adapters.out;

import com.volunteer.userservice.IntegrationBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class UserRepositoryTest extends IntegrationBase {
    @Inject
    OrganizationRepository organizationRepository;

    @Test
    void findsUserByUsername() {
        var user = userRepository.findByUsername("maria");

        assertTrue(user.isPresent());
        assertEquals("VOLUNTEER", user.get().type());
    }

    @Test
    void findsUserByEmail() {
        var user = userRepository.findByEmail("info@help-org.gr");

        assertTrue(user.isPresent());
        assertEquals("ORGANIZATION", user.get().type());
    }

    @Test
    void findsOrganizationByAfm() {
        var organization = organizationRepository.findByAfm("123456789");

        assertTrue(organization.isPresent());
        assertEquals("Help Org", organization.get().organizationName);
    }

    @Test
    void returnsEmptyForMissingUser() {
        assertTrue(userRepository.findByUsername("missing-user").isEmpty());
    }
}
