package com.hart.overwatch.checklistitem.dto;

public class CheckListItemDto {

    private Long id;

    private Long userId;

    private Long checkListId;

    private String title;

    private Boolean isCompleted;

    public CheckListItemDto() {

    }

    public CheckListItemDto(Long id, Long userId, Long checkListId, String title,
            Boolean isCompleted) {
        this.id = id;
        this.userId = userId;
        this.checkListId = checkListId;
        this.title = title;
        this.isCompleted = isCompleted;
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

    public Long getCheckListId() {
        return checkListId;
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
