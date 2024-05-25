package com.hart.overwatch.profile.response;

public class RemoveAvatarResponse {

    private String message;

    public RemoveAvatarResponse() {

    }

    public RemoveAvatarResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
