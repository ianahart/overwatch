package com.hart.overwatch.checklistitem.response;

public class DeleteCheckListItemResponse {

    private String message;

     public DeleteCheckListItemResponse() {

    }

    public DeleteCheckListItemResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
