package com.volunteer.userservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VOLUNTEER")
public class Volunteer extends User {
    public String firstName;
    public String lastName;

    @Override
    public String type() {
        return "VOLUNTEER";
    }
}
