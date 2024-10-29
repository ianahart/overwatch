package com.hart.overwatch.replycomment.response;

public class CreateReplyCommentResponse {

    private String message;

    public CreateReplyCommentResponse() {

    }

    public CreateReplyCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
