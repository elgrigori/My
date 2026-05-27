package com.volunteer.actionservice.application.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACTIVISM")
public class ActivismAction extends Action {
    public Integer minParticipants;
    public Integer maxParticipants;

    @Override
    public ActionType type() {
        return ActionType.ACTIVISM;
    }
}
