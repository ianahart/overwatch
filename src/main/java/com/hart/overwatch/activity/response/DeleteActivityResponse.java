package com.hart.overwatch.activity.response;

public class DeleteActivityResponse {

    private String message;

    public DeleteActivityResponse() {

    }

    public DeleteActivityResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
