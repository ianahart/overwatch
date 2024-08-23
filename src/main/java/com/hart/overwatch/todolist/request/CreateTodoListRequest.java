package com.hart.overwatch.todolist.request;

import jakarta.validation.constraints.Size;

public class CreateTodoListRequest {

    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    private Long userId;

    private Integer index;

    public CreateTodoListRequest() {

    }

    public CreateTodoListRequest(String title, Long userId, Integer index) {
        this.title = title;
        this.userId = userId;
        this.index = index;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIndex() {
        return index;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
