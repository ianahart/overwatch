package com.hart.overwatch.review.response;


public class CreateReviewResponse {

    private String message;



    public CreateReviewResponse() {

    }

    public CreateReviewResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
