package com.hart.overwatch.checklist.dto;

import java.time.LocalDateTime;

public class CheckListDto {

    private Long id;

    private Long userId;

    private Long todoCardId;

    private String title;

    private Boolean isCompleted;

    private LocalDateTime createdAt;

    public CheckListDto() {

    }

    public CheckListDto(Long id, Long userId, Long todoCardId, String title, Boolean isCompleted,
            LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.todoCardId = todoCardId;
        this.title = title;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
