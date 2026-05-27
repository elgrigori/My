package com.volunteer.participationservice.dto;

import java.time.LocalDateTime;

public class ActionSummary {
    public Long id;
    public String type;
    public String title;
    public String status;
    public Boolean available;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public LocalDateTime startAt;
    public LocalDateTime endAt;
}
