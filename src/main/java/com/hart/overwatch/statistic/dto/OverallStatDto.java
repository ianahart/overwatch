package com.hart.overwatch.statistic.dto;

import java.util.List;

public class OverallStatDto {

    private List<CompletedReviewStatDto> reviewsCompleted;

    public OverallStatDto() {

    }

    public OverallStatDto(List<CompletedReviewStatDto> reviewsCompleted) {
        this.reviewsCompleted = reviewsCompleted;
    }

    public List<CompletedReviewStatDto> getReviewsCompleted() {
        return reviewsCompleted;
    }

    public void setReviewsCompleted(List<CompletedReviewStatDto> reviewsCompleted) {
        this.reviewsCompleted = reviewsCompleted;
    }
}
