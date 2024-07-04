package com.hart.overwatch.connectionpin.response;

public class CreateConnectionPinResponse {

    private String message;

    public CreateConnectionPinResponse() {

    }

    public CreateConnectionPinResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
