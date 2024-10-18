package com.hart.overwatch.profile.response;

public class UpdateProfileVisibilityResponse {

    private String message;

    private Boolean data;

    public UpdateProfileVisibilityResponse() {

    }

    public UpdateProfileVisibilityResponse(String message, Boolean data) {
        this.message = message;
        this.data = data;
    }

    public Boolean getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
