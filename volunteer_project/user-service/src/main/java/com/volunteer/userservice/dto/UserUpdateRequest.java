package com.volunteer.userservice.dto;

import jakarta.validation.constraints.Email;

public class UserUpdateRequest {
    public String username;

    @Email
    public String email;

    public String password;
    public String address;
    public String city;
    public String postalCode;
    public String phone;

    public String firstName;
    public String lastName;
    public String afm;
    public String organizationName;
    public String description;
    public String mission;
    public Integer foundedYear;
}
