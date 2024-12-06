package com.hart.overwatch.ban.response;

public class DeleteBanResponse {

    private String message;

    public DeleteBanResponse() {

    }

    public DeleteBanResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
