package com.hart.overwatch.teamcomment.dto;

import java.time.LocalDateTime;

public class TeamCommentDto {

    private Long id;

    private Long userId;

    private String content;

    private LocalDateTime createdAt;

    private String fullName;

    private String avatarUrl;

    private Long teamPostId;

    public TeamCommentDto() {

    }

    public TeamCommentDto(Long id, Long userId, String content, LocalDateTime createdAt,
            String fullName, String avatarUrl, Long teamPostId) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.teamPostId = teamPostId;

    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Long getTeamPostId() {
        return teamPostId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTeamPostId(Long teamPostId) {
        this.teamPostId = teamPostId;
    }
}
