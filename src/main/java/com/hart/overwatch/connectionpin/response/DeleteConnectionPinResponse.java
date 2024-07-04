package com.hart.overwatch.connectionpin.response;

public class DeleteConnectionPinResponse {

    private String message;

    public DeleteConnectionPinResponse() {

    }

    public DeleteConnectionPinResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
