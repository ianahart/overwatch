package com.hart.overwatch.reviewfeedback.dto;

import java.time.LocalDateTime;

public class ReviewFeedbackDto {

    private Long id;

    private Long repositoryId;

    private Long ownerId;

    private Long reviewerId;

    private Integer clarity;

    private Integer responseTime;

    private Integer helpfulness;

    private Integer thoroughness;

    private LocalDateTime createdAt;


    public ReviewFeedbackDto() {

    }

    public ReviewFeedbackDto(Long id, Long repositoryId, Long ownerId, Long reviewerId,
            Integer clarity, Integer responseTime, Integer helpfulness, Integer thoroughness,
            LocalDateTime createdAt) {
        this.id = id;
        this.repositoryId = repositoryId;
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
        this.clarity = clarity;
        this.responseTime = responseTime;
        this.helpfulness = helpfulness;
        this.thoroughness = thoroughness;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Integer getClarity() {
        return clarity;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public Integer getHelpfulness() {
        return helpfulness;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public Integer getThoroughness() {
        return thoroughness;
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

    public void setClarity(Integer clarity) {
        this.clarity = clarity;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void setHelpfulness(Integer helpfulness) {
        this.helpfulness = helpfulness;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public void setThoroughness(Integer thoroughness) {
        this.thoroughness = thoroughness;
    }
}
