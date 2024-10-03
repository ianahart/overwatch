package com.hart.overwatch.repository.response;

public class UpdateRepositoryReviewStartTimeResponse {

    private String message;

    public UpdateRepositoryReviewStartTimeResponse() {

    }

    public UpdateRepositoryReviewStartTimeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
