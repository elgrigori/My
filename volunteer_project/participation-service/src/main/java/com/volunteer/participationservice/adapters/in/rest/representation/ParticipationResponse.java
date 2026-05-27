package com.volunteer.participationservice.adapters.in.rest.representation;

import com.volunteer.participationservice.application.domain.ParticipationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParticipationResponse {
    public Long id;
    public String type;
    public Long volunteerId;
    public Long actionId;
    public BigDecimal amount;
    public String productsSummary;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public ParticipationStatus status;
    public String notificationMessage;
    public Boolean notificationRead;
}
