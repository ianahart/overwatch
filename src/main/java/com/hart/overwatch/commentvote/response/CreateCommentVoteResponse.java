package com.hart.overwatch.commentvote.response;

public class CreateCommentVoteResponse {

    private String message;

    public CreateCommentVoteResponse() {

    }

    public CreateCommentVoteResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
