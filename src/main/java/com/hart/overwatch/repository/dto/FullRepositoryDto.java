package com.hart.overwatch.repository.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.repository.RepositoryStatus;
import com.hart.overwatch.repository.ReviewType;

public class FullRepositoryDto {

    private Long id;

    private Long ownerId;

    private Long reviewerId;

    private String comment;

    private String repoUrl;

    private String feedback;

    private String language;

    private String repoName;

    private String avatarUrl;

    private RepositoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime reviewStartTime;

    private LocalDateTime reviewEndTime;

    private ReviewType reviewType;

    private String reviewDuration;

    public FullRepositoryDto() {

    }

    public FullRepositoryDto(Long id, Long ownerId, Long reviewerId, String comment, String repoUrl,
            String feedback, String language, String repoName, String avatarUrl,
            RepositoryStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime reviewStartTime, LocalDateTime reviewEndTime, ReviewType reviewType,
            String reviewDuration) {
        this.id = id;
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
        this.comment = comment;
        this.repoUrl = repoUrl;
        this.feedback = feedback;
        this.language = language;
        this.repoName = repoName;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reviewStartTime = reviewStartTime;
        this.reviewEndTime = reviewEndTime;
        this.reviewType = reviewType;
        this.reviewDuration = reviewDuration;
    }

    public Long getId() {
        return id;
    }

    public String getReviewDuration() {
        return reviewDuration;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public LocalDateTime getReviewEndTime() {
        return reviewEndTime;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public LocalDateTime getReviewStartTime() {
        return reviewStartTime;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getComment() {
        return comment;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getLanguage() {
        return language;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public RepositoryStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setReviewEndTime(LocalDateTime reviewEndTime) {
        this.reviewEndTime = reviewEndTime;
    }

    public void setReviewDuration(String reviewDuration) {
        this.reviewDuration = reviewDuration;
    }

    public void setReviewStartTime(LocalDateTime reviewStartTime) {
        this.reviewStartTime = reviewStartTime;
    }


}
