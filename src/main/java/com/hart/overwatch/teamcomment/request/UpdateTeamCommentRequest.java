package com.hart.overwatch.teamcomment.request;

import jakarta.validation.constraints.Size;

public class UpdateTeamCommentRequest {

    @Size(min = 1, max = 200, message = "Comment must bet between 1 and 200 characters")
    private String content;

    public UpdateTeamCommentRequest() {

    }

    public UpdateTeamCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
