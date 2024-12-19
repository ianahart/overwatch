package com.hart.overwatch.badge.response;

public class UpdateBadgeResponse {

    private String message;

    public UpdateBadgeResponse() {

    }

    public UpdateBadgeResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
