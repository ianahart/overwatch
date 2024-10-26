package com.hart.overwatch.reportcomment.request;

import com.hart.overwatch.reportcomment.ReportReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReportCommentRequest {

    @NotNull(message = "commentId is required")
    private Long commentId;

    @NotNull(message = "userId is required")
    private Long userId;

    @Size(min = 1, max = 400, message = "Details must be between 1 and 400 characters")
    private String details;

    private ReportReason reason;

    public CreateReportCommentRequest() {

    }

    public CreateReportCommentRequest(Long commentId, Long userId, String details,
            ReportReason reason) {
        this.commentId = commentId;
        this.userId = userId;
        this.details = details;
        this.reason = reason;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getDetails() {
        return details;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }
}
