package com.hart.overwatch.comment.dto;

import java.time.LocalDateTime;


public class MinCommentDto {

    private Long id;

    private String content;

    private Long userId;

    private LocalDateTime createdAt;

    private String avatarUrl;

    private String fullName;

    public MinCommentDto() {

    }

    public MinCommentDto(Long id, String content, Long userId, LocalDateTime createdAt,
            String avatarUrl, String fullName) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
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
}
