package com.hart.overwatch.profile.response;

public class UpdateProfileResponse {

    private String message;


    public UpdateProfileResponse() {

    }

    public UpdateProfileResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
