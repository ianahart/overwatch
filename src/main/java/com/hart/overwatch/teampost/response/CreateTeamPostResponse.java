package com.hart.overwatch.teampost.response;

public class CreateTeamPostResponse {

    private String message;

    public CreateTeamPostResponse() {

    }

    public CreateTeamPostResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
