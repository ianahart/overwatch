package com.hart.overwatch.label.response;

public class DeleteLabelResponse {

    private String message;


    public DeleteLabelResponse() {

    }

    public DeleteLabelResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
