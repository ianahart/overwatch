package com.hart.overwatch.phone.dto;

import java.sql.Timestamp;

public class PhoneDto {

    private Long id;

    private Timestamp createdAt;

    private Boolean isVerified;

    private String phoneNumber;


    public PhoneDto() {

    }

    public PhoneDto(Long id, Timestamp createdAt, Boolean isVerified, String phoneNumber) {
        this.id = id;
        this.createdAt = createdAt;
        this.isVerified = isVerified;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
