package com.hart.overwatch.teaminvitation.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teaminvitation.dto.TeamInvitationDto;

public class GetTeamInvitationsResponse {

    private String message;

    private PaginationDto<TeamInvitationDto> data;

    public GetTeamInvitationsResponse() {

    }

    public GetTeamInvitationsResponse(String message, PaginationDto<TeamInvitationDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TeamInvitationDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TeamInvitationDto> data) {
        this.data = data;
    }
}
