package com.hart.overwatch.replycomment.response;

public class UpdateReplyCommentResponse {

    private String message;

    private String data;

    public UpdateReplyCommentResponse() {

    }

    public UpdateReplyCommentResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }
}
