package com.volunteer.userservice;

import com.volunteer.userservice.adapters.out.UserRepository;
import com.volunteer.userservice.application.domain.Organization;
import com.volunteer.userservice.application.domain.Volunteer;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for integration tests.
 * Resets the database and loads design-level users before each test.
 */
public abstract class IntegrationBase {
    @Inject
    protected UserRepository userRepository;

    @BeforeEach
    @Transactional
    protected void initDb() {
        userRepository.deleteAll();
        userRepository.persist(designOrganization());
        userRepository.persist(designVolunteer());
    }

    private Organization designOrganization() {
        Organization organization = new Organization();
        organization.username = "help-org";
        organization.email = "info@help-org.gr";
        organization.password = "secret1";
        organization.afm = "123456789";
        organization.organizationName = "Help Org";
        organization.description = "Community support";
        organization.mission = "Coordinate volunteer work";
        organization.foundedYear = 2010;
        organization.address = "Stadiou 10";
        organization.city = "Athens";
        organization.postalCode = "10562";
        organization.phone = "2100000000";
        return organization;
    }

    private Volunteer designVolunteer() {
        Volunteer volunteer = new Volunteer();
        volunteer.username = "maria";
        volunteer.email = "maria@example.com";
        volunteer.password = "secret1";
        volunteer.firstName = "Maria";
        volunteer.lastName = "Papadopoulou";
        volunteer.address = "Patision 76";
        volunteer.city = "Athens";
        volunteer.postalCode = "10434";
        volunteer.phone = "6900000000";
        return volunteer;
    }
}
