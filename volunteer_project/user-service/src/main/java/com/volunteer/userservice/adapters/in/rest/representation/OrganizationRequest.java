package com.volunteer.userservice.adapters.in.rest.representation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrganizationRequest {
    @NotBlank
    public String username;

    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String password;

    @NotBlank
    public String afm;

    @NotBlank
    public String organizationName;

    public String description;
    public String mission;
    public Integer foundedYear;
    public String address;
    public String city;
    public String postalCode;
    public String phone;
}
