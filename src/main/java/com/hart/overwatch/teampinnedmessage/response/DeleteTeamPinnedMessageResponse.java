package com.hart.overwatch.teampinnedmessage.response;

public class DeleteTeamPinnedMessageResponse {

    private String message;

    public DeleteTeamPinnedMessageResponse() {

    }

    public DeleteTeamPinnedMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
