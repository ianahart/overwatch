package com.hart.overwatch.replycomment.dto;

import java.time.LocalDateTime;


public class ReplyCommentDto {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private String fullName;

    private String avatarUrl;

    private Long userId;

    public ReplyCommentDto() {

    }

    public ReplyCommentDto(Long id, String content, LocalDateTime createdAt, String fullName,
            String avatarUrl, Long userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.userId = userId;
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
