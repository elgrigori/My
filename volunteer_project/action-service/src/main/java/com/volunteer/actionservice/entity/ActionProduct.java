package com.volunteer.actionservice.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class ActionProduct {
    public String name;
    public Integer targetQuantity;
    public Integer remainingQuantity;
}
