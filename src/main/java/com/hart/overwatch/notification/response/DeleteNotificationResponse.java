package com.hart.overwatch.notification.response;

public class DeleteNotificationResponse {

    private String message;


    public DeleteNotificationResponse() {

    }

    public DeleteNotificationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
