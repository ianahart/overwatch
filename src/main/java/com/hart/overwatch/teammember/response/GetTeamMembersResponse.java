package com.hart.overwatch.teammember.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teammember.dto.TeamMemberDto;

public class GetTeamMembersResponse {

    private String message;

    private PaginationDto<TeamMemberDto> data;

    private TeamMemberDto admin;

    public GetTeamMembersResponse() {

    }

    public GetTeamMembersResponse(String message, PaginationDto<TeamMemberDto> data,
            TeamMemberDto admin) {
        this.message = message;
        this.data = data;
        this.admin = admin;
    }

    public String getMessage() {
        return message;
    }

    public TeamMemberDto getAdmin() {
        return admin;
    }

    public PaginationDto<TeamMemberDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAdmin(TeamMemberDto admin) {
        this.admin = admin;
    }

    public void setData(PaginationDto<TeamMemberDto> data) {
        this.data = data;
    }

}
