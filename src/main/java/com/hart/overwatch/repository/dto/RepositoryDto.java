package com.hart.overwatch.repository.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.repository.RepositoryStatus;

public class RepositoryDto {

    private Long id;

    private Long ownerId;

    private Long reviewerId;

    private String firstName;

    private String lastName;

    private String profileUrl;

    private String repoName;

    private String language;

    private String repoUrl;

    private String avatarUrl;

    private LocalDateTime createdAt;

    private RepositoryStatus status;

    private LocalDateTime reviewStartTime;

    private LocalDateTime reviewEndTime;

    private String feedback;

    private Double paymentPrice; 

    public RepositoryDto() {

    }

    public RepositoryDto(Long id, Long ownerId, Long reviewerId, String firstName, String lastName,
            String profileUrl, String repoName, String language, String repoUrl, String avatarUrl,
            LocalDateTime createdAt, RepositoryStatus status, LocalDateTime reviewStartTime,
            LocalDateTime reviewEndTime, String feedback, Double paymentPrice) {

        this.id = id;
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileUrl = profileUrl;
        this.repoName = repoName;
        this.language = language;
        this.repoUrl = repoUrl;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.status = status;
        this.reviewStartTime = reviewStartTime;
        this.reviewEndTime = reviewEndTime;
        this.feedback = feedback;
        this.paymentPrice = paymentPrice;
    }

    public Long getId() {
        return id;
    }

    public String getFeedback() {
        return feedback;
    }

    public Double getPaymentPrice() {
        return paymentPrice;
    }

    public LocalDateTime getReviewEndTime() {
        return reviewEndTime;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getReviewStartTime() {
        return reviewStartTime;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public RepositoryStatus getStatus() {
        return status;
    }

    public String getLanguage() {
        return language;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRepoName() {
        return repoName;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }

    public void setReviewEndTime(LocalDateTime reviewEndTime) {
        this.reviewEndTime = reviewEndTime;
    }

    public void setReviewStartTime(LocalDateTime reviewStartTime) {
        this.reviewStartTime = reviewStartTime;
    }

    public void setPaymentPrice(Double paymentPrice) {
        this.paymentPrice = paymentPrice;
    }
}
