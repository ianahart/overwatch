package com.hart.overwatch.repository.response;

public class UpdateRepositoryCommentResponse {

    private String message;

    public UpdateRepositoryCommentResponse() {

    }

    public UpdateRepositoryCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
