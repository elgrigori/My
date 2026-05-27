package com.volunteer.actionservice.adapters.in.rest.representation;

import com.volunteer.actionservice.application.domain.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ActionRequest {
    @NotNull
    public ActionType type;

    public Long organizationId;

    @NotBlank
    public String title;

    public String description;

    public LocalDateTime startDate;

    public LocalDateTime startAt;

    public LocalDateTime endDate;

    public LocalDateTime endAt;

    @NotBlank
    public String location;

    public String category;

    public Integer minParticipants;
    public Integer maxParticipants;
    public Integer totalParticipants;
    public String requiredItems;
    public List<ProductRequest> products;
    public BigDecimal targetAmount;
}
