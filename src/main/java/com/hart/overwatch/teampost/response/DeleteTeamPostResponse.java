package com.hart.overwatch.teampost.response;

public class DeleteTeamPostResponse {
    private String message;

    public DeleteTeamPostResponse() {

    }

    public DeleteTeamPostResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
