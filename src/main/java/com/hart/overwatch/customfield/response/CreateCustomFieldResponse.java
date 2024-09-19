package com.hart.overwatch.customfield.response;

public class CreateCustomFieldResponse {

    private String message;

    public CreateCustomFieldResponse() {

    }

    public CreateCustomFieldResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
