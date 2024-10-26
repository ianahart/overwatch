package com.hart.overwatch.comment.dto;

import java.time.LocalDateTime;


public class CommentDto {

    private Long id;

    private String content;

    private Long userId;

    private LocalDateTime createdAt;

    private String avatarUrl;

    private String fullName;

    private Boolean isEdited;

    private Long voteDifference;

    private Boolean curUserHasVoted;

    private String curUserVoteType;

    private Boolean curUserHasSaved;


    public CommentDto() {

    }

    public CommentDto(Long id, String content, Long userId, LocalDateTime createdAt,
            String avatarUrl, String fullName, Boolean isEdited, Long voteDifference) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.isEdited = isEdited;
        this.voteDifference = voteDifference;
    }

    public String getCurUserVoteType() {
        return curUserVoteType;
    }

    public Boolean getCurUserHasVoted() {
        return curUserHasVoted;
    }

    public Boolean getCurUserHasSaved() {
        return curUserHasSaved;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public Long getId() {
        return id;
    }

    public Long getVoteDifference() {
        return voteDifference;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setVoteDifference(Long voteDifference) {
        this.voteDifference = voteDifference;
    }

    public void setCurUserVoteType(String curUserVoteType) {
        this.curUserVoteType = curUserVoteType;
    }

    public void setCurUserHasVoted(Boolean curUserHasVoted) {
        this.curUserHasVoted = curUserHasVoted;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setCurUserHasSaved(Boolean curUserHasSaved) {
        this.curUserHasSaved = curUserHasSaved;
    }
}
