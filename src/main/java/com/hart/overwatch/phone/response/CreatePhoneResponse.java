package com.hart.overwatch.phone.response;

public class CreatePhoneResponse {

    private String message;


    public CreatePhoneResponse() {

    }


    public CreatePhoneResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
