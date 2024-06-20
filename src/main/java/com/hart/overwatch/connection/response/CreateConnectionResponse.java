package com.hart.overwatch.connection.response;

public class CreateConnectionResponse {

    private String message;


    public CreateConnectionResponse() {

    }

    public CreateConnectionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
