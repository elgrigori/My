package com.volunteer.actionservice.application.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class ActionProduct {
    public String name;
    public Integer targetQuantity;
    public Integer remainingQuantity;
}
