package com.hart.overwatch.repository;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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


@Entity
@Table(name = "repository")
public class Repository {

    @Id
    @SequenceGenerator(name = "repository_sequence", sequenceName = "repository_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repository_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "avatar_url", length = 400)
    private String avatarUrl;

    @Column(name = "repo_url", length = 400)
    private String repoUrl;

    @Column(name = "repo_name", length = 200)
    private String repoName;

    @Column(name = "language", length = 100)
    private String language;

    @Enumerated(EnumType.STRING)
    private RepositoryStatus status;

    @ManyToOne()
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    private User reviewer;

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;


    public Repository() {

    }

    public Repository(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String feedback,
            String comment, String avatarUrl, String repoUrl, String repoName, String language,
            RepositoryStatus status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.feedback = feedback;
        this.comment = comment;
        this.avatarUrl = avatarUrl;
        this.repoUrl = repoUrl;
        this.repoName = repoName;
        this.language = language;
        this.status = status;

    }

    public Repository(String feedback, String comment, String avatarUrl, String repoUrl,
            String repoName, String language, RepositoryStatus status, User reviewer, User owner) {
        this.feedback = feedback;
        this.comment = comment;
        this.avatarUrl = avatarUrl;
        this.repoUrl = repoUrl;
        this.repoName = repoName;
        this.language = language;
        this.status = status;
        this.reviewer = reviewer;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getLanguage() {
        return language;
    }

    public User getReviewer() {
        return reviewer;
    }

    public String getComment() {
        return comment;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public RepositoryStatus getStatus() {
        return status;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setStatus(RepositoryStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
