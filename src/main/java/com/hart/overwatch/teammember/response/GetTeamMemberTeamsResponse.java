package com.hart.overwatch.teammember.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teammember.dto.TeamMemberTeamDto;

public class GetTeamMemberTeamsResponse {

    private String message;

    private PaginationDto<TeamMemberTeamDto> data;

    private Long totalTeamMemberTeams;

    public GetTeamMemberTeamsResponse() {

    }

    public GetTeamMemberTeamsResponse(String message, PaginationDto<TeamMemberTeamDto> data,
            Long totalTeamMemberTeams) {
        this.message = message;
        this.data = data;
        this.totalTeamMemberTeams = totalTeamMemberTeams;
    }

    public String getMessage() {
        return message;
    }

    public Long getTotalTeamMemberTeams() {
        return totalTeamMemberTeams;
    }

    public PaginationDto<TeamMemberTeamDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TeamMemberTeamDto> data) {
        this.data = data;
    }

    public void setTotalTeamMemberTeams(Long totalTeamMemberTeams) {
        this.totalTeamMemberTeams = totalTeamMemberTeams;
    }
}
