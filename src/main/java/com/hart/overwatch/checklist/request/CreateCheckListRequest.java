package com.hart.overwatch.checklist.request;

import jakarta.validation.constraints.Size;

public class CreateCheckListRequest {

    private Long userId;

    private Long todoCardId;

    @Size(max = 100, message = "Checklist title must be under 100 characters")
    private String title;

    public CreateCheckListRequest() {

    }

    public CreateCheckListRequest(Long userId, Long todoCardId, String title) {
        this.userId = userId;
        this.todoCardId = todoCardId;
        this.title = title;
    }

    public Long gettodoCardId() {
        return todoCardId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void settodoCardId(Long todoCardId) {
        this.todoCardId = todoCardId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
