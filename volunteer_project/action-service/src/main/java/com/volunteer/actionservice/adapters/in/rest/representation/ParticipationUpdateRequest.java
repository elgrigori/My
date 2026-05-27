package com.volunteer.actionservice.adapters.in.rest.representation;

import java.math.BigDecimal;
import java.util.List;

public class ParticipationUpdateRequest {
    public BigDecimal amount;
    public List<ProductContributionRequest> products;
}
