package com.volunteer.participationservice.application.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "participations")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long volunteerId;

    @Column(nullable = false)
    public Long actionId;

    public String type;

    public BigDecimal amount;

    @Column(length = 2000)
    public String productsSummary;

    @Column(nullable = false)
    public LocalDateTime startDate;

    @Column(nullable = false)
    public LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public ParticipationStatus status;

    @Column(length = 1000)
    public String notificationMessage;

    @Column(length = 4000)
    public String notificationHistory;

    public boolean notificationRead;
}
