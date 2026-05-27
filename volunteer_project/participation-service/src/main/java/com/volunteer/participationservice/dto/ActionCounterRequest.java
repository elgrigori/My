package com.volunteer.participationservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class ActionCounterRequest {
    public BigDecimal amount;
    public List<ProductContributionRequest> products;
}
