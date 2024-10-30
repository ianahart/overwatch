package com.hart.overwatch.replycomment.response;

public class DeleteReplyCommentResponse {

    private String message;

    public DeleteReplyCommentResponse() {

    }

    public DeleteReplyCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
