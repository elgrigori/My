package com.volunteer.actionservice.application.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDateTime;

@Entity
@Table(name = "actions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "action_type")
public abstract class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;

    @Column(length = 4000)
    public String description;

    @Column(nullable = false)
    public LocalDateTime startDate;

    @Column(nullable = false)
    public LocalDateTime endDate;

    @Column(nullable = false)
    public String location;

    @Column(nullable = false)
    public String category;

    public Long organizationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ActionStatus status = ActionStatus.OPEN;

    public Integer currentParticipants = 0;

    public abstract ActionType type();
}
