package com.hart.overwatch.reaction.response;

public class CreateReactionResponse {

    private String message;

    public CreateReactionResponse() {

    }

    public CreateReactionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
