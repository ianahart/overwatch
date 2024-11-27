package com.hart.overwatch.teaminvitation.response;

public class DeleteTeamInvitationResponse {

    private String message;

    public DeleteTeamInvitationResponse() {

    }

    public DeleteTeamInvitationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
