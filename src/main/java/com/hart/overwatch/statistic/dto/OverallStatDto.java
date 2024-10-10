package com.hart.overwatch.statistic.dto;

import java.util.List;
import java.util.Map;

public class OverallStatDto {

    private List<CompletedReviewStatDto> reviewsCompleted;
    private List<ReviewTypeStatDto> reviewTypesCompleted;
    private List<Map<String, Object>> avgReviewTimes;
    private List<ReviewFeedbackRatingStatDto> avgRatings;
    private List<LanguageStatDto> mainLanguages;
    private List<StatusTypeStatDto> statusTypes;


    public OverallStatDto() {

    }

    public OverallStatDto(List<CompletedReviewStatDto> reviewsCompleted,
            List<ReviewTypeStatDto> reviewTypesCompleted, List<Map<String, Object>> avgReviewTimes,
            List<ReviewFeedbackRatingStatDto> avgRatings, List<LanguageStatDto> mainLanguages,
            List<StatusTypeStatDto> statusTypes) {
        this.reviewsCompleted = reviewsCompleted;
        this.reviewTypesCompleted = reviewTypesCompleted;
        this.avgReviewTimes = avgReviewTimes;
        this.avgRatings = avgRatings;
        this.mainLanguages = mainLanguages;
        this.statusTypes = statusTypes;
    }

    public List<CompletedReviewStatDto> getReviewsCompleted() {
        return reviewsCompleted;
    }

    public List<LanguageStatDto> getMainLanguages() {
        return mainLanguages;
    }

    public List<ReviewFeedbackRatingStatDto> getAvgRatings() {
        return avgRatings;
    }

    public List<Map<String, Object>> getAvgReviewTimes() {
        return avgReviewTimes;
    }

    public List<StatusTypeStatDto> getStatusTypes() {
        return statusTypes;
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

    public void setAvgRatings(List<ReviewFeedbackRatingStatDto> avgRatings) {
        this.avgRatings = avgRatings;
    }

    public void setMainLanguages(List<LanguageStatDto> mainLanguages) {
        this.mainLanguages = mainLanguages;
    }

    public void setStatusTypes(List<StatusTypeStatDto> statusTypes) {
        this.statusTypes = statusTypes;
    }
}
