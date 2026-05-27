package com.volunteer.userservice.repository;

import com.volunteer.userservice.entity.Organization;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepository<Organization> {
    public Optional<Organization> findByAfm(String afm) {
        return find("afm", afm).firstResultOptional();
    }
}
