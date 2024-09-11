package com.hart.overwatch.checklist.response;

public class DeleteCheckListResponse {

    private String message;

    public DeleteCheckListResponse() {

    }

    public DeleteCheckListResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
