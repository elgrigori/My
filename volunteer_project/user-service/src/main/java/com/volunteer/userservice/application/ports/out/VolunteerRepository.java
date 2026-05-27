package com.volunteer.userservice.application.ports.out;

import com.volunteer.userservice.application.domain.Volunteer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface VolunteerRepository extends PanacheRepository<Volunteer> {
}
