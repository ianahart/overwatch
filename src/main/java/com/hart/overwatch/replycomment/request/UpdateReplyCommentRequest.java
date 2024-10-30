package com.hart.overwatch.replycomment.request;

import jakarta.validation.constraints.Size;

public class UpdateReplyCommentRequest {

    @Size(min = 1, max = 400, message = "Reply comment must be between 1 and 400 characters") 
    private String content;

    public UpdateReplyCommentRequest() {

    }

    public UpdateReplyCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
