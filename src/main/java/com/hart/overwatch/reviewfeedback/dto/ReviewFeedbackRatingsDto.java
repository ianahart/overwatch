package com.hart.overwatch.reviewfeedback.dto;

public class ReviewFeedbackRatingsDto {

    private Integer thoroughness;

    private Integer helpfulness;

    private Integer responseTime;

    private Integer clarity;

    public ReviewFeedbackRatingsDto() {

    }

    public ReviewFeedbackRatingsDto(Integer thoroughness, Integer helpfulness, Integer responseTime,
            Integer clarity) {
        this.thoroughness = thoroughness;
        this.helpfulness = helpfulness;
        this.responseTime = responseTime;
        this.clarity = clarity;
    }

    public Integer getClarity() {
        return clarity;
    }

    public Integer getHelpfulness() {
        return helpfulness;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public Integer getThoroughness() {
        return thoroughness;
    }

    public void setClarity(Integer clarity) {
        this.clarity = clarity;
    }

    public void setHelpfulness(Integer helpfulness) {
        this.helpfulness = helpfulness;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public void setThoroughness(Integer thoroughness) {
        this.thoroughness = thoroughness;
    }
}
