package com.hart.overwatch.connection.response;

public class DeleteConnectionResponse {

    private String message;

    public DeleteConnectionResponse() {

    }

    public DeleteConnectionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
