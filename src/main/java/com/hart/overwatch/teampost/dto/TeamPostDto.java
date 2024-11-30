package com.hart.overwatch.teampost.dto;

import java.time.LocalDateTime;

public class TeamPostDto {

    private Long id;

    private Long teamId;

    private LocalDateTime createdAt;

    private Long userId;

    private String code;

    private Boolean isEdited;

    private String fullName;

    private String avatarUrl;

    private String language;

    private Boolean hasComments;


    public TeamPostDto() {

    }

    public TeamPostDto(Long id, Long teamId, LocalDateTime createdAt, Long userId, String code,
            Boolean isEdited, String fullName, String avatarUrl, String language,
            Boolean hasComments) {

        this.id = id;
        this.teamId = teamId;
        this.createdAt = createdAt;
        this.userId = userId;
        this.code = code;
        this.isEdited = isEdited;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.language = language;
        this.hasComments = hasComments;
    }

    public Long getId() {
        return id;
    }

    public Boolean getHasComments() {
        return hasComments;
    }


    public String getLanguage() {
        return language;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getCode() {
        return code;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setHasComments(Boolean hasComments) {
        this.hasComments = hasComments;
    }


}
