package com.hart.overwatch.comment.response;

public class UpdateCommentResponse {

    private String message;


    public UpdateCommentResponse() {

    }

    public UpdateCommentResponse(String message) {
         this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
