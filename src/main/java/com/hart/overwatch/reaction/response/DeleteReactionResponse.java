package com.hart.overwatch.reaction.response;

public class DeleteReactionResponse {

    private String message;

    public DeleteReactionResponse() {

    }

    public DeleteReactionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
