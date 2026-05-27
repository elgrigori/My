package com.volunteer.actionservice.application.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("FUNDING")
public class FundingAction extends Action {
    public BigDecimal targetAmount;
    public BigDecimal raisedAmount = BigDecimal.ZERO;

    @Override
    public ActionType type() {
        return ActionType.FUNDING;
    }
}
