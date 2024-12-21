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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((reportedBy == null) ? 0 : reportedBy.hashCode());
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReportComment other = (ReportComment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        if (reason != other.reason)
            return false;
        if (status != other.status)
            return false;
        if (reportedBy == null) {
            if (other.reportedBy != null)
                return false;
        } else if (!reportedBy.equals(other.reportedBy))
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        return true;
    }


}
