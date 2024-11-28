package com.hart.overwatch.teammember.response;

public class DeleteTeamMemberResponse {

    private String message;

    public DeleteTeamMemberResponse() {

    }

    public DeleteTeamMemberResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
