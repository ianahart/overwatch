package com.hart.overwatch.badge.response;

public class DeleteBadgeResponse {

    private String message;

    public DeleteBadgeResponse() {

    }

    public DeleteBadgeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
