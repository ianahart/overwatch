package com.hart.overwatch.reportcomment.dto;

import java.time.LocalDateTime;
import com.hart.overwatch.reportcomment.ReportReason;
import com.hart.overwatch.reportcomment.ReportStatus;

public class ReportCommentDto {

    private Long id;

    private String details;

    private ReportReason reason;

    private ReportStatus status;

    private String reportedBy;

    private LocalDateTime createdAt;

    private String content;

    private String commentAvatarUrl;

    private String topicTitle;


    public ReportCommentDto() {

    }

    public ReportCommentDto(Long id, String details, ReportReason reason, ReportStatus status,
            String reportedBy, LocalDateTime createdAt, String content, String commentAvatarUrl,
            String topicTitle) {

        this.id = id;
        this.details = details;
        this.reason = reason;
        this.status = status;
        this.reportedBy = reportedBy;
        this.createdAt = createdAt;
        this.content = content;
        this.commentAvatarUrl = commentAvatarUrl;
        this.topicTitle = topicTitle;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDetails() {
        return details;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public ReportReason getReason() {
        return reason;
    }

    public String getCommentAvatarUrl() {
        return commentAvatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCommentAvatarUrl(String commentAvatarUrl) {
        this.commentAvatarUrl = commentAvatarUrl;
    }

}
