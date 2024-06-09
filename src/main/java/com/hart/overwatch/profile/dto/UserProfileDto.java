package com.hart.overwatch.profile.dto;

import com.hart.overwatch.user.Role;

public class UserProfileDto {

    private Long id;

    private Long userId;

    private Role role;

    private String country;

    private String abbreviation;

    private String city;


    public UserProfileDto() {

    }

    public UserProfileDto(Long id, Long userId, Role role, String country, String abbreviation,
            String city) {
        this.id = id;
        this.userId = userId;
        this.role = role;
        this.country = country;
        this.abbreviation = abbreviation;
        this.city = city;
    }


    public Long getId() {
        return id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getCountry() {
        return country;
    }

    public Long getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}
