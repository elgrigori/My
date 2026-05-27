package com.volunteer.actionservice.dto;

import com.volunteer.actionservice.entity.ActionStatus;
import com.volunteer.actionservice.entity.ActionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ActionResponse {
    public Long id;
    public ActionType type;
    public Long organizationId;
    public String title;
    public String description;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public LocalDateTime startAt;
    public LocalDateTime endAt;
    public String location;
    public String category;
    public ActionStatus status;
    public Integer currentParticipants;
    public Integer minParticipants;
    public Integer maxParticipants;
    public Integer totalParticipants;
    public String requiredItems;
    public List<ProductResponse> products;
    public BigDecimal targetAmount;
    public BigDecimal raisedAmount;
    public Boolean available;
}
