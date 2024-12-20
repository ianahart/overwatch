package com.hart.overwatch.reviewerbadge;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.badge.Badge;
import com.hart.overwatch.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviewer_badge")
public class ReviewerBadge {

    @Id
    @SequenceGenerator(name = "reviewer_badge_sequence", sequenceName = "reviewer_badge_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewer_badge_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id", nullable = false)
    private User reviewer;

    @ManyToOne()
    @JoinColumn(name = "badge_id", referencedColumnName = "id", nullable = false)
    private Badge badge;

    public ReviewerBadge() {

    }

    public ReviewerBadge(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ReviewerBadge(User reviewer, Badge badge) {
        this.reviewer = reviewer;
        this.badge = badge;
    }


    public Long getId() {
        return id;
    }

    public Badge getBadge() {
        return badge;
    }

    public User getReviewer() {
        return reviewer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
