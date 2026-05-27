package com.volunteer.userservice.application.ports.out;

import com.volunteer.userservice.application.domain.Organization;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.Optional;

public interface OrganizationRepository extends PanacheRepository<Organization> {
    Optional<Organization> findByAfm(String afm);
}
