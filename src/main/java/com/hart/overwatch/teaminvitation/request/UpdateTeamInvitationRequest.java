package com.hart.overwatch.teaminvitation.request;

import jakarta.validation.constraints.NotNull;

public class UpdateTeamInvitationRequest {

    @NotNull
    private Long userId;

    public UpdateTeamInvitationRequest() {

    }

    public UpdateTeamInvitationRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
