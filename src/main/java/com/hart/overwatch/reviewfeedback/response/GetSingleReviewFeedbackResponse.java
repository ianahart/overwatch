package com.hart.overwatch.reviewfeedback.response;

import com.hart.overwatch.reviewfeedback.dto.ReviewFeedbackDto;

public class GetSingleReviewFeedbackResponse {

    private String message;

    private ReviewFeedbackDto data;

    public GetSingleReviewFeedbackResponse() {

    }

    public GetSingleReviewFeedbackResponse(String message, ReviewFeedbackDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public ReviewFeedbackDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(ReviewFeedbackDto data) {
        this.data = data;
    }
}
