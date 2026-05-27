package com.volunteer.userservice.adapters.in.rest.representation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VolunteerRequest {
    @NotBlank
    public String username;

    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String password;

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    public String address;
    public String city;
    public String postalCode;
    public String phone;
}
