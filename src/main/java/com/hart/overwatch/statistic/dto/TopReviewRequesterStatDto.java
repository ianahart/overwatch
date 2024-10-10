package com.hart.overwatch.statistic.dto;

public class TopReviewRequesterStatDto {

    private String fullName;

    private Integer count;

    public TopReviewRequesterStatDto() {

    }

    public TopReviewRequesterStatDto(String fullName, Integer count) {
        this.fullName = fullName;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public String getFullName() {
        return fullName;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
