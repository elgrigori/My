package com.volunteer.userservice.adapters.out;

import com.volunteer.userservice.application.domain.Organization;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrganizationRepository implements com.volunteer.userservice.application.ports.out.OrganizationRepository {
    public Optional<Organization> findByAfm(String afm) {
        return find("afm", afm).firstResultOptional();
    }
}
