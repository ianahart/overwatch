package com.hart.overwatch.dropdownoption.response;

public class DeleteDropDownOptionResponse {

    private String message;

    public DeleteDropDownOptionResponse() {

    }

    public DeleteDropDownOptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
