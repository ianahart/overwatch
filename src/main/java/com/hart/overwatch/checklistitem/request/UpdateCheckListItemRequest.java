package com.hart.overwatch.checklistitem.request;

import jakarta.validation.constraints.Size;

public class UpdateCheckListItemRequest {

    private Long checkListId;

    private Long id;

    private Long userId;

    @Size(max = 50, message = "Check List Item must be a maximum of 50 characters")
    private String title;

    private Boolean isCompleted;


    public UpdateCheckListItemRequest() {

    }

    public UpdateCheckListItemRequest(Long checkListId, Long id, Long userId, String title,
            Boolean isCompleted) {
        this.checkListId = checkListId;
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCheckListId() {
        return checkListId;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
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

    public void setCheckListId(Long checkListId) {
        this.checkListId = checkListId;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
