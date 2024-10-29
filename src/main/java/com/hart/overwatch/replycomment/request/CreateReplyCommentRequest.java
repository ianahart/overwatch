package com.hart.overwatch.replycomment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReplyCommentRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @Size(min = 1, max = 400, message = "Reply comment must be between 1 and 400 characters")
    private String content;

    public CreateReplyCommentRequest() {

    }

    public CreateReplyCommentRequest(Long userId, String content) {
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
