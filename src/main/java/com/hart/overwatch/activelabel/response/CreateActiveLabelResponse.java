package com.hart.overwatch.activelabel.response;

public class CreateActiveLabelResponse {

    private String message;


    public CreateActiveLabelResponse() {

    }

    public CreateActiveLabelResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
