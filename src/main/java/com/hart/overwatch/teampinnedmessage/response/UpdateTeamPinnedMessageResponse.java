package com.hart.overwatch.teampinnedmessage.response;

public class UpdateTeamPinnedMessageResponse {

    private String message;

    public UpdateTeamPinnedMessageResponse() {

    }

    public UpdateTeamPinnedMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
