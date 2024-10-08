package com.hart.overwatch.repository.dto;

import java.time.LocalDateTime;

public class CompletedRepositoryReviewDto {

    private Long id;

    private LocalDateTime reviewEndTime;

    public CompletedRepositoryReviewDto() {

    }

    public CompletedRepositoryReviewDto(Long id, LocalDateTime reviewEndTime) {
        this.id = id;
        this.reviewEndTime = reviewEndTime;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getReviewEndTime() {
        return reviewEndTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReviewEndTime(LocalDateTime reviewEndTime) {
        this.reviewEndTime = reviewEndTime;
    }
}
