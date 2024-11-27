package com.hart.overwatch.teaminvitation.response;

public class CreateTeamInvitationResponse {

    private String message;

    public CreateTeamInvitationResponse() {

    }

    public CreateTeamInvitationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
