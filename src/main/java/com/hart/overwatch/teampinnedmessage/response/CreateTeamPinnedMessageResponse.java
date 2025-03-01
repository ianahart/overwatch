package com.hart.overwatch.teampinnedmessage.response;

public class CreateTeamPinnedMessageResponse {

    private String message;

    public CreateTeamPinnedMessageResponse() {

    }

    public CreateTeamPinnedMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
