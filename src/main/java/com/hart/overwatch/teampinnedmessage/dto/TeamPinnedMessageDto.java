package com.hart.overwatch.teampinnedmessage.dto;

import java.time.LocalDateTime;

public class TeamPinnedMessageDto {

    private Long id;

    private Long userId;

    private String fullName;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private String message;

    private Boolean isEdited;

    private LocalDateTime updatedAt;


    public TeamPinnedMessageDto(Long id, Long userId, String fullName, String avatarUrl,
            LocalDateTime createdAt, String message, Boolean isEdited, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.message = message;
        this.isEdited = isEdited;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
