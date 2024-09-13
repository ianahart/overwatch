package com.hart.overwatch.activity.dto;

import java.time.LocalDateTime;

public class ActivityDto {

    private Long id;

    private Long userId;

    private Long todoCardId;

    private LocalDateTime createdAt;

    private String text;

    private String avatarUrl;

    public ActivityDto() {

    }

    public ActivityDto(Long id, Long userId, Long todoCardId, LocalDateTime createdAt, String text,
            String avataUrl) {
        this.id = id;
        this.userId = userId;
        this.todoCardId = todoCardId;
        this.createdAt = createdAt;
        this.text = text;
        this.avatarUrl = avataUrl;
    }


    public Long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTodoCardId() {
        return todoCardId;
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

    public void setTodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
