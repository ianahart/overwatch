package com.hart.overwatch.activity.request;

import jakarta.validation.constraints.Size;

public class CreateActivityRequest {

    private Long todoCardId;

    private Long userId;

    @Size(max = 200, message = "An activity must be under 200 characters")
    private String text;

    public CreateActivityRequest() {

    }

    public CreateActivityRequest(Long todoCardId, Long userId, String text) {
        this.todoCardId = todoCardId;
        this.userId = userId;
        this.text = text;
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

    public void setText(String text) {
        this.text = text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }
}
