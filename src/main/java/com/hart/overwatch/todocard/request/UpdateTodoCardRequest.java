package com.hart.overwatch.todocard.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;


public class UpdateTodoCardRequest {
    private Long id;

    private LocalDateTime createdAt;

    @Size(max = 100, message = "Label must be under 100 characters")
    private String label;

    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    @Size(max = 100, message = "Color must be under 100 characters")
    private String color;

    private Integer index;

    @Size(max = 1000, message = "Details must be under 1000 characters")
    private String details;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String photo;

    private String uploadPhotoUrl;


    public UpdateTodoCardRequest() {

    }

    public UpdateTodoCardRequest(Long id, LocalDateTime createdAt, String label, String title,
            String color, Integer index, String details, LocalDateTime startDate,
            LocalDateTime endDate, String photo, String uploadPhotoUrl) {
        this.id = id;
        this.createdAt = createdAt;
        this.label = label;
        this.title = title;
        this.color = color;
        this.index = index;
        this.details = details;
        this.startDate = startDate;
        this.endDate = endDate;
        this.photo = photo;
        this.uploadPhotoUrl = uploadPhotoUrl;
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getUploadPhotoUrl() {
        return uploadPhotoUrl;
    }

    public String getLabel() {
        return label;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIndex() {
        return index;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setUploadPhotoUrl(String uploadPhotoUrl) {
        this.uploadPhotoUrl = uploadPhotoUrl;
    }
}
