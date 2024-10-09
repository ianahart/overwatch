package com.hart.overwatch.statistic.dto;

import java.util.List;
import java.util.Map;

public class OverallStatDto {

    private List<CompletedReviewStatDto> reviewsCompleted;
    private List<ReviewTypeStatDto> reviewTypesCompleted;
    private List<Map<String, Object>> avgReviewTimes;


    public OverallStatDto() {

    }

    public OverallStatDto(List<CompletedReviewStatDto> reviewsCompleted,
            List<ReviewTypeStatDto> reviewTypesCompleted, List<Map<String, Object>> avgReviewTimes) {
        this.reviewsCompleted = reviewsCompleted;
        this.reviewTypesCompleted = reviewTypesCompleted;
        this.avgReviewTimes = avgReviewTimes;
    }

    public List<CompletedReviewStatDto> getReviewsCompleted() {
        return reviewsCompleted;
    }

    public List<Map<String, Object>> getAvgReviewTimes() {
        return avgReviewTimes;
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

    public void setAvgReviewTimes(List<Map<String, Object>> avgReviewTimes) {
        this.avgReviewTimes = avgReviewTimes;
    }
}
