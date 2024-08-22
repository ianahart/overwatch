package com.hart.overwatch.workspace.request;



import jakarta.validation.constraints.Size;

public class UpdateWorkSpaceRequest {

    private Long userId;

    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    @Size(max = 10, message = "Background color must be under 10 characters")
    private String backgroundColor;

    public UpdateWorkSpaceRequest() {

    }

    public UpdateWorkSpaceRequest(Long userId, String title, String backgroundColor) {
        this.userId = userId;
        this.title = title;
        this.backgroundColor = backgroundColor;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}


