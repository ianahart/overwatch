package com.hart.overwatch.team.response;

public class CreateTeamResponse {

    private String message;

    public CreateTeamResponse() {

    }

    public CreateTeamResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
