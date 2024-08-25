package com.hart.overwatch.todolist.request;

import jakarta.validation.constraints.Size;

public class UpdateTodoListRequest {

    private Integer index;

    @Size(max = 50, message = "Todo list title must be under 50 characters")
    private String title;

    private Long workSpaceId;

    public UpdateTodoListRequest() {

    }

    public UpdateTodoListRequest(Integer index, String title, Long workSpaceId) {
        this.index = index;
        this.title = title;
        this.workSpaceId = workSpaceId;
    }

    public Integer getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public Long getWorkSpaceId() {
        return workSpaceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setWorkSpaceId(Long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }
}
