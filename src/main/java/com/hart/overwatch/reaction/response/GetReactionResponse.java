package com.hart.overwatch.reaction.response;

public class GetReactionResponse {

    private String message;

    private String data;

    public GetReactionResponse() {

    }

    public GetReactionResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
