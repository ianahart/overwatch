package com.hart.overwatch.authentication.request;

import jakarta.validation.constraints.Size;

public class RegisterRequest {
    private String email;
    @Size(min = 1, max = 200, message = "First name must be between 1 and 200 characters")
    private String firstName;
    @Size(min = 1, max = 200, message = "Last name must be between 1 and 200 characters")
    private String lastName;
    private String password;
    private String confirmPassword;
    private String role;

    public RegisterRequest() {

    }

    public RegisterRequest(String email, String firstName, String lastName, String password,
            String confirmPassword, String role) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

