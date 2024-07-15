package com.hart.overwatch.repository.dto;

import java.time.LocalDateTime;

public class RepositoryDto {

    private Long id;

    private Long ownerId;

    private Long reviewerId;

    private String firstName;

    private String lastName;

    private String profileUrl;

    private String repoName;

    private String language;

    private String repoUrl;

    private String avatarUrl;

    private LocalDateTime createdAt;

    public RepositoryDto() {

    }

    public RepositoryDto(Long id, Long ownerId, Long reviewerId, String firstName, String lastName,
            String profileUrl, String repoName, String language, String repoUrl, String avatarUrl,
            LocalDateTime createdAt) {

        this.id = id;
        this.ownerId = ownerId;
        this.reviewerId = reviewerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileUrl = profileUrl;
        this.repoName = repoName;
        this.language = language;
        this.repoUrl = repoUrl;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getLanguage() {
        return language;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRepoName() {
        return repoName;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
