package com.hart.overwatch.checklist.response;

public class CreateCheckListResponse {

    private String message;

    public CreateCheckListResponse() {

    }

    public CreateCheckListResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
