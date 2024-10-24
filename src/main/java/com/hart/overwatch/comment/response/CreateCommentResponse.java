package com.hart.overwatch.comment.response;

public class CreateCommentResponse {

    private String message;

    public CreateCommentResponse() {

    }

    public CreateCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
