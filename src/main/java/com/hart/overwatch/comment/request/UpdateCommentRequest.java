package com.hart.overwatch.comment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCommentRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @Size(min = 1, max = 400, message = "Comment must be from 1 to 400 characters")
    private String content;

    public UpdateCommentRequest() {

    }

    public UpdateCommentRequest(Long userId, String content) {
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
