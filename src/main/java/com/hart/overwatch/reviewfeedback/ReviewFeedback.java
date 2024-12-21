package com.hart.overwatch.reviewfeedback;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.hart.overwatch.repository.Repository;
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

@Entity()
@Table(name = "review_feedback")
public class ReviewFeedback {

    @Id
    @SequenceGenerator(name = "review_feedback_sequence", sequenceName = "review_feedback_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_feedback_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "clarity")
    private Integer clarity;

    @Column(name = "helpfulness")
    private Integer helpfulness;

    @Column(name = "thoroughness")
    private Integer thoroughness;

    @Column(name = "response_time")
    private Integer responseTime;

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne()
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    private User reviewer;

    @ManyToOne()
    @JoinColumn(name = "repository_id", referencedColumnName = "id")
    private Repository repository;

    public ReviewFeedback() {

    }

    public ReviewFeedback(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
            Integer clarity, Integer thoroughness, Integer responseTime, Integer helpfulness) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.clarity = clarity;
        this.thoroughness = thoroughness;
        this.responseTime = responseTime;
        this.helpfulness = helpfulness;
    }

    public ReviewFeedback(Integer clarity, Integer thoroughness, Integer responseTime,
            Integer helpfulness, User owner, User reviewer, Repository repository) {
        this.clarity = clarity;
        this.thoroughness = thoroughness;
        this.responseTime = responseTime;
        this.helpfulness = helpfulness;
        this.owner = owner;
        this.reviewer = reviewer;
        this.repository = repository;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public User getReviewer() {
        return reviewer;
    }

    public Integer getClarity() {
        return clarity;
    }

    public Integer getHelpfulness() {
        return helpfulness;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public Integer getThoroughness() {
        return thoroughness;
    }

    public Repository getRepository() {
        return repository;
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

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setClarity(Integer clarity) {
        this.clarity = clarity;
    }

    public void setHelpfulness(Integer helpfulness) {
        this.helpfulness = helpfulness;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public void setThoroughness(Integer thoroughness) {
        this.thoroughness = thoroughness;
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
        result = prime * result + ((clarity == null) ? 0 : clarity.hashCode());
        result = prime * result + ((helpfulness == null) ? 0 : helpfulness.hashCode());
        result = prime * result + ((thoroughness == null) ? 0 : thoroughness.hashCode());
        result = prime * result + ((responseTime == null) ? 0 : responseTime.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((reviewer == null) ? 0 : reviewer.hashCode());
        result = prime * result + ((repository == null) ? 0 : repository.hashCode());
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
        ReviewFeedback other = (ReviewFeedback) obj;
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
        if (clarity == null) {
            if (other.clarity != null)
                return false;
        } else if (!clarity.equals(other.clarity))
            return false;
        if (helpfulness == null) {
            if (other.helpfulness != null)
                return false;
        } else if (!helpfulness.equals(other.helpfulness))
            return false;
        if (thoroughness == null) {
            if (other.thoroughness != null)
                return false;
        } else if (!thoroughness.equals(other.thoroughness))
            return false;
        if (responseTime == null) {
            if (other.responseTime != null)
                return false;
        } else if (!responseTime.equals(other.responseTime))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (reviewer == null) {
            if (other.reviewer != null)
                return false;
        } else if (!reviewer.equals(other.reviewer))
            return false;
        if (repository == null) {
            if (other.repository != null)
                return false;
        } else if (!repository.equals(other.repository))
            return false;
        return true;
    }

}
