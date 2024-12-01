package com.hart.overwatch.teamcomment.request;

import jakarta.validation.constraints.Size;

public class UpdateTeamCommentRequest {

    @Size(min = 1, max = 200, message = "Comment must bet between 1 and 200 characters")
    private String content;

    @Size(min = 0, max = 200, message = "Tag name must be between 1 and 200 characters")
    private String tag;

    public UpdateTeamCommentRequest() {

    }

    public UpdateTeamCommentRequest(String content, String tag) {
        this.content = content;
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
