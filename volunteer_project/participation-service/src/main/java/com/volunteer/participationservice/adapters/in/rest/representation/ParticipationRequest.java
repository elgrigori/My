package com.volunteer.participationservice.adapters.in.rest.representation;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class ParticipationRequest {
    public String type;

    @NotNull
    public Long volunteerId;

    @NotNull
    public Long actionId;

    public BigDecimal amount;
    public List<ProductContributionRequest> products;
}
