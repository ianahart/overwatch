package com.hart.overwatch.repository.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.repository.RepositoryStatus;
import com.hart.overwatch.repository.ReviewType;

public class CompletedRepositoryReviewDto {

    private Long id;

    private LocalDateTime reviewEndTime;

    private ReviewType reviewType;

    private LocalDateTime reviewStartTime;

    public CompletedRepositoryReviewDto() {

    }

    public CompletedRepositoryReviewDto(Long id, LocalDateTime reviewEndTime, ReviewType reviewType,
            LocalDateTime reviewStartTime) {
        this.id = id;
        this.reviewEndTime = reviewEndTime;
        this.reviewType = reviewType;
        this.reviewStartTime = reviewStartTime;
    }

    public Long getId() {
        return id;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public LocalDateTime getReviewEndTime() {
        return reviewEndTime;
    }

    public LocalDateTime getReviewStartTime() {
        return reviewStartTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReviewEndTime(LocalDateTime reviewEndTime) {
        this.reviewEndTime = reviewEndTime;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public void setReviewStartTime(LocalDateTime reviewStartTime) {
        this.reviewStartTime = reviewStartTime;
    }
}
