package com.hart.overwatch.workspace.dto;

import java.sql.Timestamp;

public class WorkSpaceDto {

    private Long id;

    private Long userId;

    private Timestamp createdAt;

    private String title;

    private String backgroundColor;


    public WorkSpaceDto() {

    }

    public WorkSpaceDto(Long id, Long userId, Timestamp createdAt, String title,
            String backgroundColor) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.title = title;
        this.backgroundColor = backgroundColor;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getBackgroundColor() {
        return backgroundColor;
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
