package com.hart.overwatch.passwordreset.response;

public class PasswordResetResponse {

    private String message;


    public PasswordResetResponse() {

    }

    public PasswordResetResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
