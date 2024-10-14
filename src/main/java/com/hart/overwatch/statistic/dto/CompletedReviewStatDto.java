package com.hart.overwatch.statistic.dto;

public class CompletedReviewStatDto {

    private String day;

    private Integer reviewsCompleted;

    public CompletedReviewStatDto() {

    }

    public CompletedReviewStatDto(String day, Integer reviewsCompleted) {
        this.day = day;
        this.reviewsCompleted = reviewsCompleted;
    }

    public String getDay() {
        return day;
    }

    public Integer getReviewsCompleted() {
        return reviewsCompleted;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setReviewsCompleted(Integer reviewsCompleted) {
        this.reviewsCompleted = reviewsCompleted;
    }
}
