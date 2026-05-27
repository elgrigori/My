package com.volunteer.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORGANIZATION")
public class Organization extends User {
    @Column(unique = true)
    public String afm;

    public String organizationName;

    @Column(length = 2000)
    public String description;

    @Column(length = 2000)
    public String mission;

    public Integer foundedYear;

    @Override
    public String type() {
        return "ORGANIZATION";
    }
}
