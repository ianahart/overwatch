package com.hart.overwatch.customfield.response;

public class DeleteCustomFieldResponse {

    private String message;

    public DeleteCustomFieldResponse() {

    }

    public DeleteCustomFieldResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
