package com.hart.overwatch.ban.response;

public class CreateBanResponse {

    private String message;

    public CreateBanResponse() {

    }

    public CreateBanResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
