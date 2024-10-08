package com.hart.overwatch.statistic.dto;

public class CompletedReviewStatDto {

    private Integer day;

    private Integer reviewsCompleted;

    public CompletedReviewStatDto() {

    }

    public CompletedReviewStatDto(Integer day, Integer reviewsCompleted) {
        this.day = day;
        this.reviewsCompleted = reviewsCompleted;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getReviewsCompleted() {
        return reviewsCompleted;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setReviewsCompleted(Integer reviewsCompleted) {
        this.reviewsCompleted = reviewsCompleted;
    }
}
