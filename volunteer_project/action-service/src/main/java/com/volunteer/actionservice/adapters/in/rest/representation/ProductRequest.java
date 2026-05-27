package com.volunteer.actionservice.adapters.in.rest.representation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ProductRequest {
    @NotBlank
    public String name;

    @Positive
    public Integer targetQuantity;
}
