package com.hart.overwatch.teammember.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teammember.dto.TeamMemberDto;

public class SearchTeamMembersResponse {

    private String message;

    private PaginationDto<TeamMemberDto> data;

    public SearchTeamMembersResponse() {

    }

    public SearchTeamMembersResponse(String message, PaginationDto<TeamMemberDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TeamMemberDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TeamMemberDto> data) {
        this.data = data;
    }
}
