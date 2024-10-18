package com.hart.overwatch.profile.response;

public class GetProfileVisibilityResponse {

    private String message;

    private Boolean data;

    public GetProfileVisibilityResponse() {

    }

    public GetProfileVisibilityResponse(String message, Boolean data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
