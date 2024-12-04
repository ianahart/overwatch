package com.hart.overwatch.teammessage.dto;

import java.time.LocalDateTime;


public class TeamMessageDto {

    private Long id;

    private String text;

    private LocalDateTime createdAt;

    private Long userId;

    private String fullName;

    private String avatarUrl;

    private Long teamId;


    public TeamMessageDto() {

    }

    public TeamMessageDto(Long id, String text, LocalDateTime createdAt, Long userId,
            String fullName, String avatarUrl, Long teamId) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.userId = userId;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.teamId = teamId;
    }

    public Long getId() {
        return id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
