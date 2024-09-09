package com.hart.overwatch.label.response;

public class CreateLabelResponse {

    private String message;

    public CreateLabelResponse() {

    }

    public CreateLabelResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
