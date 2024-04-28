package com.hart.overwatch.user.dto;

import com.hart.overwatch.user.Role;

public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String abbreviation;
    private Boolean loggedIn;
    private Long profileId;
    private String avatarUrl;
    private String fullName;
    private Long settingId;
    private String slug;

    public UserDto() {

    }

    public UserDto(Long id, String email, String firstName, String lastName, Role role,
            String abbreviation, Boolean loggedIn, Long profileId, String avatarUrl,
            String fullName, Long settingId, String slug) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.abbreviation = abbreviation;
        this.loggedIn = loggedIn;
        this.profileId = profileId;
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.settingId = settingId;
        this.slug = slug;
    }

    public Long getSettingId() {
        return settingId;
    }

    public String getSlug() {
        return slug;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}

