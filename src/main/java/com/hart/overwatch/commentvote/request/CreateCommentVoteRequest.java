package com.hart.overwatch.commentvote.request;

import com.hart.overwatch.commentvote.validator.ValidVoteType;
import jakarta.validation.constraints.NotNull;

public class CreateCommentVoteRequest {

    @NotNull(message = "commentId is required")
    private Long commentId;

    @NotNull(message = "userId is required")
    private Long userId;

    @ValidVoteType
    private String voteType;

    public CreateCommentVoteRequest() {

    }

    public CreateCommentVoteRequest(Long commentId, Long userId, String voteType) {
        this.commentId = commentId;
        this.userId = userId;
        this.voteType = voteType;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
}
