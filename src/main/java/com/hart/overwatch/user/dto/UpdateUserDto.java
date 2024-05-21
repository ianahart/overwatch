package com.hart.overwatch.user.dto;

public class UpdateUserDto {

    private String firstName;

    private String lastName;

    private String email;

    private String abbreviation;


    public UpdateUserDto(String firstName, String lastName, String email, String abbreviation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.abbreviation = abbreviation;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
