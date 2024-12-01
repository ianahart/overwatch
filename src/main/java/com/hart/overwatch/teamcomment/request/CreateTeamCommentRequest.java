package com.hart.overwatch.teamcomment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTeamCommentRequest {

    @NotNull(message = "Missing requried user Id")
    private Long userId;

    @Size(min = 1, max = 200, message = "Content must be between 1 and 200 characters")
    private String content;

    @Size(min = 0, max = 200, message = "Tag name must bet between 1 and 200 characters")
    private String tag;

    public CreateTeamCommentRequest() {

    }

    public CreateTeamCommentRequest(Long userId, String content, String tag) {
        this.userId = userId;
        this.content = content;
        this.tag = tag;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTag() {
        return tag;
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

    public void setTag(String tag) {
        this.tag = tag;
    }
}
