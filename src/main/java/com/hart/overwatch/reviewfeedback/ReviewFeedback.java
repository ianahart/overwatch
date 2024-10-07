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

}
