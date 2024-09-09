package com.hart.overwatch.label.dto;

import java.time.LocalDateTime;


public class LabelDto {

    private Long id;

    private Long workSpaceId;

    private Long userId;

    private LocalDateTime createdAt;

    private Boolean isChecked;

    private String title;

    private String color;


    public LabelDto() {

    }

    public LabelDto(Long id, Long workSpaceId, Long userId, LocalDateTime createdAt,
            Boolean isChecked, String title, String color) {
        this.id = id;
        this.workSpaceId = workSpaceId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.isChecked = isChecked;
        this.title = title;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getWorkSpaceId() {
        return workSpaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void setWorkSpaceId(Long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
