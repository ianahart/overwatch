package com.hart.overwatch.activelabel.response;

public class DeleteActiveLabelResponse {

    private String message;


    public DeleteActiveLabelResponse() {

    }

    public DeleteActiveLabelResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
