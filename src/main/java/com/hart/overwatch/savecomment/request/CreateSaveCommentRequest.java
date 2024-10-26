package com.hart.overwatch.savecomment.request;

import jakarta.validation.constraints.NotNull;

public class CreateSaveCommentRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "commentId is required")
    private Long commentId;

    public CreateSaveCommentRequest() {

    }

    public CreateSaveCommentRequest(Long userId, Long commentId) {
         this.userId = userId;
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
