package com.hart.overwatch.comment.response;

public class DeleteCommentResponse {

    private String message;

    public DeleteCommentResponse() {

    }

    public DeleteCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
