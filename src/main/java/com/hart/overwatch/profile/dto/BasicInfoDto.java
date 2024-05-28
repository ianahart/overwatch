package com.hart.overwatch.profile.dto;

public class BasicInfoDto {

    private String fullName;

    private String userName;

    private String email;

    private String contactNumber;


    public BasicInfoDto() {

    }

    public BasicInfoDto(String fullName, String userName, String email, String contactNumber) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
