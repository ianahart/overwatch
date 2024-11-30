package com.hart.overwatch.teamcomment.response;

public class CreateTeamCommentResponse {

    private String message;

    public CreateTeamCommentResponse() {

    }

    public CreateTeamCommentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
