package com.volunteer.userservice.repository;

import com.volunteer.userservice.entity.Volunteer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VolunteerRepository implements PanacheRepository<Volunteer> {
}
