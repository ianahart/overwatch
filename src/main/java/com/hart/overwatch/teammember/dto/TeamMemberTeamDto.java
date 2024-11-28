package com.hart.overwatch.teammember.dto;

public class TeamMemberTeamDto {

    private Long id;

    private Long userId;

    private Long teamId;

    private String teamName;


    public TeamMemberTeamDto() {

    }

    public TeamMemberTeamDto(Long id, Long userId, Long teamId, String teamName) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public Long getId() {
        return id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}


