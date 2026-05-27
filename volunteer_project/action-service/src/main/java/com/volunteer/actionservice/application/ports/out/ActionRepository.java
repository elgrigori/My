package com.volunteer.actionservice.application.ports.out;

import com.volunteer.actionservice.application.domain.Action;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface ActionRepository extends PanacheRepository<Action> {
}
