package com.hart.overwatch.team.dto;

public class TeamDto {

    private Long id;

    private Long userId;

    private String teamName;

    private String teamDescription;

    private Long totalTeams;

    public TeamDto() {

    }

    public TeamDto(Long id, Long userId, String teamName, String teamDescription, Long totalTeams) {
        this.id = id;
        this.userId = userId;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.totalTeams = totalTeams;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTotalTeams() {
        return totalTeams;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setTotalTeams(Long totalTeams) {
        this.totalTeams = totalTeams;
    }
}
