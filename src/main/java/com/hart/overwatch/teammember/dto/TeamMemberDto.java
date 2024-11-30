package com.hart.overwatch.teammember.dto;

import java.time.LocalDateTime;

public class TeamMemberDto {

    private Long id;

    private Long userId;

    private Long teamId;

    private Long profileId;

    private String fullName;

    private String avatarUrl;

    private LocalDateTime createdAt;


    public TeamMemberDto() {

    }

    public TeamMemberDto(Long id, Long userId, Long teamId, Long profileId, String fullName,
            String avatarUrl, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
        this.profileId = profileId;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
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

    public Long getProfileId() {
        return profileId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
