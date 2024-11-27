package com.hart.overwatch.teaminvitation.response;

public class UpdateTeamInvitationResponse {

    private String message;

    public UpdateTeamInvitationResponse() {

    }

    public UpdateTeamInvitationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
