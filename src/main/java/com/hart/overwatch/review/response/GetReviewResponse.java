package com.hart.overwatch.review.response;

import com.hart.overwatch.review.dto.MinReviewDto;

public class GetReviewResponse {

    private String message;

    private MinReviewDto data;

    public GetReviewResponse() {

    }

    public GetReviewResponse(String message, MinReviewDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public MinReviewDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(MinReviewDto data) {
        this.data = data;
    }
}
