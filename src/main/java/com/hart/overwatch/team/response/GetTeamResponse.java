package com.hart.overwatch.team.response;

import com.hart.overwatch.team.dto.TeamDto;

public class GetTeamResponse {

    private String message;

    private TeamDto data;

    public GetTeamResponse(String message, TeamDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public TeamDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(TeamDto data) {
        this.data = data;
    }
}
