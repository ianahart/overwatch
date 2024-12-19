package com.hart.overwatch.badge.dto;

import java.time.LocalDateTime;

public class BadgeDto {

    private Long id;

    private LocalDateTime createdAt;

    private String title;

    private String description;

    private String imageUrl;

    public BadgeDto() {

    }

    public BadgeDto(Long id, LocalDateTime createdAt, String title, String description,
            String imageUrl) {
        this.id = id;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
