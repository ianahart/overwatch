package com.hart.overwatch.phone.response;

public class DeletePhoneResponse {

    private String message;


    public DeletePhoneResponse() {

    }

    public DeletePhoneResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
