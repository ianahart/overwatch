package com.hart.overwatch.savecomment.response;

public class DeleteSaveCommentResponse {

    private String message;

    public DeleteSaveCommentResponse() {

    }

    public DeleteSaveCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
