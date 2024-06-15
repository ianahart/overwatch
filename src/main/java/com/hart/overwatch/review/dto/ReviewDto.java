package com.hart.overwatch.review.dto;

import java.sql.Timestamp;

public class ReviewDto {

    private Long id;

    private Long authorId;

    private String avatarUrl;

    private Byte rating;

    private String review;

    private Timestamp createdAt;

    private Boolean isEdited;

    private String name;

    public ReviewDto() {

    }

    public ReviewDto(Long id, Long authorId, String avatarUrl, Byte rating, String review,
            Timestamp createdAt, Boolean isEdited, String name) {
        this.id = id;
        this.authorId = authorId;
        this.avatarUrl = avatarUrl;
        this.rating = rating;
        this.review = review;
        this.createdAt = createdAt;
        this.isEdited = isEdited;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Byte getRating() {
        return rating;
    }

    public Long getAuthorId() {
        return authorId;
    }


    public String getReview() {
        return review;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        this.name = name;
    } 
}

