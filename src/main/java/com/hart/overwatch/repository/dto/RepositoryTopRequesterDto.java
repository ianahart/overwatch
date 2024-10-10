package com.hart.overwatch.repository.dto;

import java.time.LocalDateTime;

public class RepositoryTopRequesterDto {

    private Long ownerId;

    private String fullName;

    private LocalDateTime reviewStartTime;

    public RepositoryTopRequesterDto() {

    }

    public RepositoryTopRequesterDto(Long ownerId, String fullName, LocalDateTime reviewStartTime) {
        this.ownerId = ownerId;
        this.fullName = fullName;
        this.reviewStartTime = reviewStartTime;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getReviewStartTime() {
        return reviewStartTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setReviewStartTime(LocalDateTime reviewStartTime) {
        this.reviewStartTime = reviewStartTime;
    }
}
