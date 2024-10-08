package com.hart.overwatch.statistic.dto;

import com.hart.overwatch.repository.ReviewType;

public class ReviewTypeStatDto {

    private ReviewType reviewType;

    private Integer count;


    public ReviewTypeStatDto() {

    }

    public ReviewTypeStatDto(ReviewType reviewType, Integer count) {
        this.reviewType = reviewType;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }
}
