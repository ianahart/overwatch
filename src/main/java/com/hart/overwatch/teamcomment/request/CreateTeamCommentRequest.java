package com.hart.overwatch.teamcomment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTeamCommentRequest {

    @NotNull(message = "Missing requried user Id")
    private Long userId;

    @Size(min = 1, max = 200, message = "Content must be between 1 and 200 characters")
    private String content;

    public CreateTeamCommentRequest() {

    }

    public CreateTeamCommentRequest(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
