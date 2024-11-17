package com.hart.overwatch.reportcomment;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity()
@Table(name = "report_comment")
public class ReportComment {

    @Id
    @SequenceGenerator(name = "report_comment_sequence", sequenceName = "report_comment_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_comment_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "details", length = 400, nullable = false)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_reason", nullable = false)
    private ReportReason reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status")
    private ReportStatus status;

    @ManyToOne()
    @JoinColumn(name = "reported_by_user_id", nullable = false, referencedColumnName = "id")
    private User reportedBy;

    @ManyToOne()
    @JoinColumn(name = "comment_id", nullable = false, referencedColumnName = "id")
    private Comment comment;

    public ReportComment() {

    }

    public ReportComment(Long id, LocalDateTime createdAt, ReportReason reason, String details,
            ReportStatus status) {
        this.id = id;
        this.createdAt = createdAt;
        this.reason = reason;
        this.details = details;
        this.status = status;
    }

    public ReportComment(ReportStatus status, ReportReason reason, String details, Comment comment,
            User reportedBy) {
        this.status = status;
        this.reason = reason;
        this.details = details;
        this.comment = comment;
        this.reportedBy = reportedBy;
    }

    public Long getId() {
        return id;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public Comment getComment() {
        return comment;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReportReason getReason() {
        return reason;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setReason(ReportReason reason) {
        this.reason = reason;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }
}
