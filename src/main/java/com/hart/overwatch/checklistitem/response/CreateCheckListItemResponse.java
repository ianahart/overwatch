package com.hart.overwatch.checklistitem.response;

public class CreateCheckListItemResponse {

    private String message;

    public CreateCheckListItemResponse() {

    }

    public CreateCheckListItemResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
