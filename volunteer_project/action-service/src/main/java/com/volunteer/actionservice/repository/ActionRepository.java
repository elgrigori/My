package com.volunteer.actionservice.repository;

import com.volunteer.actionservice.entity.Action;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ActionRepository implements PanacheRepository<Action> {
}
