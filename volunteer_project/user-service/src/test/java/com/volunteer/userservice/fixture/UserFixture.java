package com.volunteer.userservice.fixture;

import com.volunteer.userservice.adapters.in.rest.representation.OrganizationRequest;
import com.volunteer.userservice.adapters.in.rest.representation.VolunteerRequest;
import com.volunteer.userservice.application.domain.Organization;
import com.volunteer.userservice.application.domain.Volunteer;

public final class UserFixture {
    private UserFixture() {
    }

    public static Volunteer volunteer(String username, String email) {
        Volunteer volunteer = new Volunteer();
        volunteer.username = username;
        volunteer.email = email;
        volunteer.password = "secret1";
        volunteer.firstName = "Ada";
        volunteer.lastName = "Lovelace";
        volunteer.city = "Athens";
        return volunteer;
    }

    public static Organization organization(String username, String email, String afm) {
        Organization organization = new Organization();
        organization.username = username;
        organization.email = email;
        organization.password = "secret1";
        organization.afm = afm;
        organization.organizationName = "Helping Hands";
        organization.description = "Community support";
        organization.mission = "Coordinate volunteer work";
        organization.foundedYear = 2015;
        organization.city = "Athens";
        return organization;
    }

    public static VolunteerRequest volunteerRequest(String username, String email) {
        VolunteerRequest request = new VolunteerRequest();
        request.username = username;
        request.email = email;
        request.password = "secret1";
        request.firstName = "Ada";
        request.lastName = "Lovelace";
        request.city = "Athens";
        return request;
    }

    public static OrganizationRequest organizationRequest(String username, String email, String afm) {
        OrganizationRequest request = new OrganizationRequest();
        request.username = username;
        request.email = email;
        request.password = "secret1";
        request.afm = afm;
        request.organizationName = "Helping Hands";
        request.description = "Community support";
        request.mission = "Coordinate volunteer work";
        request.foundedYear = 2015;
        request.city = "Athens";
        return request;
    }
}
