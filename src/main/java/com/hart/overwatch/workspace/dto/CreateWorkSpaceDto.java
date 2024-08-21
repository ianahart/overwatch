package com.hart.overwatch.workspace.dto;

public class CreateWorkSpaceDto {

    private String title;

    private String backgroundColor;

    public CreateWorkSpaceDto() {

    }

    public CreateWorkSpaceDto(String title, String backgroundColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
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

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
