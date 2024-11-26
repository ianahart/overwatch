package com.hart.overwatch.team.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTeamRequest {

    @NotNull(message = "userId is required in the payload")
    private Long userId;

    @Size(min = 1, max = 100, message = "Team name must be between 1 and 100 characters")
    private String teamName;

    @Size(min = 1, max = 200, message = "Team description must be between 1 and 200 characters")
    private String teamDescription;

    public CreateTeamRequest() {

    }

    public CreateTeamRequest(Long userId, String teamName, String teamDescription) {
        this.userId = userId;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }
}
