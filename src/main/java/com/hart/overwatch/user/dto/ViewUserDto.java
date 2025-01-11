package com.hart.overwatch.user.dto;

import java.sql.Timestamp;
import com.hart.overwatch.user.Role;

public class ViewUserDto {

    private Long id;

    private Timestamp createdAt;

    private String firstName;

    private String lastName;

    private String avatarUrl;

    private Role role;

    private String email;

    public ViewUserDto() {

    }

    public ViewUserDto(Long id, Timestamp createdAt, String firstName, String lastName,
            String avatarUrl, Role role, String email) {
        this.id = id;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
