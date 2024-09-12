package com.hart.overwatch.checklistitem.response;

public class UpdateCheckListItemResponse {

    private String message;


    public UpdateCheckListItemResponse() {

    }

    public UpdateCheckListItemResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
