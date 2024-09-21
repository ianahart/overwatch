package com.hart.overwatch.customfield.response;

public class UpdateCustomFieldResponse {

    private String message;

    public UpdateCustomFieldResponse() {

    }

    public UpdateCustomFieldResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
