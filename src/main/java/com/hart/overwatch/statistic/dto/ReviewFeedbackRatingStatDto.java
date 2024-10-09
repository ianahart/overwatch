package com.hart.overwatch.statistic.dto;

public class ReviewFeedbackRatingStatDto {

    private String name;

    private Double average;


    public ReviewFeedbackRatingStatDto() {

    }

    public ReviewFeedbackRatingStatDto(String name, Double average) {
        this.name = name;
        this.average = average;
    }

    public String getName() {
        return name;
    }

    public Double getAverage() {
        return average;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
