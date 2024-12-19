package com.hart.overwatch.badge.response;

public class CreateBadgeResponse {

    private String message;

    public CreateBadgeResponse() {

    }

    public CreateBadgeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
