package com.hart.overwatch.checklistitem.request;

import jakarta.validation.constraints.Size;

public class CreateCheckListItemRequest {

    private Long checkListId;

    private Long userId;

    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    public CreateCheckListItemRequest() {

    }

    public CreateCheckListItemRequest(Long checkListId, Long userId, String title) {
        this.checkListId = checkListId;
        this.userId = userId;
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Long getCheckListId() {
        return checkListId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCheckListId(Long checkListId) {
        this.checkListId = checkListId;
    }
}
