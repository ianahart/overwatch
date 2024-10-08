package com.hart.overwatch.statistic.dto;

import java.util.List;

public class OverallStatDto {

    private List<CompletedReviewStatDto> reviewsCompleted;
    private List<ReviewTypeStatDto> reviewTypesCompleted;

    public OverallStatDto() {

    }

    public OverallStatDto(List<CompletedReviewStatDto> reviewsCompleted,
            List<ReviewTypeStatDto> reviewTypesCompleted) {
        this.reviewsCompleted = reviewsCompleted;
        this.reviewTypesCompleted = reviewTypesCompleted;
    }

    public List<CompletedReviewStatDto> getReviewsCompleted() {
        return reviewsCompleted;
    }

    public List<ReviewTypeStatDto> getReviewTypesCompleted() {
        return reviewTypesCompleted;
    }

    public void setReviewsCompleted(List<CompletedReviewStatDto> reviewsCompleted) {
        this.reviewsCompleted = reviewsCompleted;
    }

    public void setReviewTypesCompleted(List<ReviewTypeStatDto> reviewTypesCompleted) {
        this.reviewTypesCompleted = reviewTypesCompleted;
    }
}
