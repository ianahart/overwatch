package com.hart.overwatch.label.request;

import jakarta.validation.constraints.Size;

public class CreateLabelRequest {

    private Long userId;

    private Long workSpaceId;

    @Size(max = 25, message = "Label must be under 25 characters")
    private String title;

    @Size(max = 10, message = "Color must be under 10 characters")
    private String color;


    public CreateLabelRequest() {

    }

    public CreateLabelRequest(Long userId, Long workSpaceId, String title, String color) {
        this.userId = userId;
        this.workSpaceId = workSpaceId;
        this.title = title;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Long getWorkSpaceId() {
        return workSpaceId;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setWorkSpaceId(Long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }
}
