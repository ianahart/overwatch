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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((reviewer == null) ? 0 : reviewer.hashCode());
        result = prime * result + ((badge == null) ? 0 : badge.hashCode());
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
        ReviewerBadge other = (ReviewerBadge) obj;
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
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        if (reviewer == null) {
            if (other.reviewer != null)
                return false;
        } else if (!reviewer.equals(other.reviewer))
            return false;
        if (badge == null) {
            if (other.badge != null)
                return false;
        } else if (!badge.equals(other.badge))
            return false;
        return true;
    }

}
