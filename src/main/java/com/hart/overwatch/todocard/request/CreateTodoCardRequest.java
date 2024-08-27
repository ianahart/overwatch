package com.hart.overwatch.todocard.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class CreateTodoCardRequest {

    @Size(max = 100)
    private String title;

    @Min(value = 0)
    private Integer index;

    private Long userId;

    public CreateTodoCardRequest(String title, Integer index, Long userId) {
        this.title = title;
        this.index = index;
        this.userId = userId;
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
