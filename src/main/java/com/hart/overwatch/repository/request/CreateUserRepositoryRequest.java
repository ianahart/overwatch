package com.hart.overwatch.repository.request;

import jakarta.validation.constraints.Size;

public class CreateUserRepositoryRequest {

    private Long reviewerId;

    private Long ownerId;

    private String repoName;

    private String repoUrl;

    private String avatarUrl;

    private String language;

    @Size(max = 500, message = "Comment must be under 500 characters")
    private String comment;


    public CreateUserRepositoryRequest() {

    }

    public CreateUserRepositoryRequest(Long reviewerId, Long ownerId, String repoName,
            String repoUrl, String avatarUrl, String comment, String language) {
        this.reviewerId = reviewerId;
        this.ownerId = ownerId;
        this.repoName = repoName;
        this.repoUrl = repoUrl;
        this.avatarUrl = avatarUrl;
        this.comment = comment;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getComment() {
        return comment;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
