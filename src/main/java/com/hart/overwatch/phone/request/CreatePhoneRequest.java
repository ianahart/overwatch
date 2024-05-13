package com.hart.overwatch.phone.request;

import jakarta.validation.constraints.Pattern;

public class CreatePhoneRequest {

    private Long userId;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
    private String phoneNumber;


    public CreatePhoneRequest() {

    }

    public CreatePhoneRequest(Long userId, String phoneNumber) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
