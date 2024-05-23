package com.hart.overwatch.location.response;

public class CreateLocationResponse {

    private String message;

    public CreateLocationResponse() {

    }

    public CreateLocationResponse(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
