package com.hart.overwatch.reviewfeedback.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateReviewFeedbackRequest {

    @NotNull(message = "OwnerId is required")
    private Long ownerId;

    @NotNull(message = "ReviewerId is required")
    private Long reviewerId;

    @NotNull(message = "RepositoryId is required")
    private Long repositoryId;

    @NotNull(message = "Clarity rating is required")
    @Min(value = 1, message = "Clarity rating must be at least 1")
    @Max(value = 5, message = "Clarity rating must not exceed 5")
    private Integer clarity;

    @NotNull(message = "Helpfulness rating is required")
    @Min(value = 1, message = "Helpfulness rating must be at least 1")
    @Max(value = 5, message = "Helpfulness rating must not exceed 5")
    private Integer helpfulness;

    @NotNull(message = "Thoroughness rating is required")
    @Min(value = 1, message = "Thoroughness rating must be at least 1")
    @Max(value = 5, message = "Thoroughness rating must not exceed 5")
    private Integer thoroughness;

    @NotNull(message = "Response Time rating is required")
    @Min(value = 1, message = "Response Time rating must be at least 1")
    @Max(value = 5, message = "Response Time rating must not exceed 5")
    private Integer responseTime;

    public CreateReviewFeedbackRequest() {

    }

    public CreateReviewFeedbackRequest(Long ownerId, Long reviewerId, Long repositoryId,
            Integer clarity, Integer helpfulness, Integer responseTime, Integer thoroughness) {
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
        this.repositoryId = repositoryId;
        this.clarity = clarity;
        this.helpfulness = helpfulness;
        this.responseTime = responseTime;
        this.thoroughness = thoroughness;
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

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public void setThoroughness(Integer thoroughness) {
        this.thoroughness = thoroughness;
    }
}
