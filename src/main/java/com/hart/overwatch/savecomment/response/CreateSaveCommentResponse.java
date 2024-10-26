package com.hart.overwatch.savecomment.response;

public class CreateSaveCommentResponse {

    private String message;

    public CreateSaveCommentResponse() {

    }

    public CreateSaveCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
