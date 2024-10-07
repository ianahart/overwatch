package com.hart.overwatch.reviewfeedback.response;

public class CreateReviewFeedbackResponse {

    private String message;

    public CreateReviewFeedbackResponse() {

    }

    public CreateReviewFeedbackResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
