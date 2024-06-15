package com.hart.overwatch.review;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
@Table(name = "review")
public class Review {

    @Id
    @SequenceGenerator(name = "review_sequence", sequenceName = "review_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_sequence")
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @Column(name = "rating")
    private Byte rating;

    @Column(name = "review", length = 400)
    private String review;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    public Review() {

    }


    public Review(Long id, Timestamp createdAt, Timestamp updatedAt, Boolean isEdited, Byte rating,
            String review) {

        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isEdited = isEdited;
        this.rating = rating;
        this.review = review;
    }

    public Review(User author, User reviewer, Boolean isEdited, Byte rating, String review) {
        this.author = author;
        this.reviewer = reviewer;
        this.isEdited = isEdited;
        this.rating = rating;
        this.review = review;
    }


    public Long getId() {
        return id;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsEdited() {
        return isEdited;
    }

    public Byte getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public User getReviewer() {
        return reviewer;
    }

    public User getAuthor() {
        return author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


}


