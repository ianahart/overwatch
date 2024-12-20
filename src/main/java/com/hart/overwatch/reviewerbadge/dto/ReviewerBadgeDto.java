package com.hart.overwatch.reviewerbadge.dto;

import java.time.LocalDateTime;

public class ReviewerBadgeDto {

    private Long id;

    private Long reviewerId;

    private Long badgeId;

    private LocalDateTime createdAt;

    private String title;

    private String description;

    private String imageUrl;

    public ReviewerBadgeDto() {

    }

    public ReviewerBadgeDto(Long id, Long reviewerId, Long badgeId, LocalDateTime createdAt,
            String title, String description, String imageUrl) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.badgeId = badgeId;
        this.createdAt = createdAt;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getBadgeId() {
        return badgeId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getReviewerId() {
        return reviewerId;
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

    public void setBadgeId(Long badgeId) {
        this.badgeId = badgeId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
