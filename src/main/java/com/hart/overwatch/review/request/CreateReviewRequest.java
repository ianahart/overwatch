package com.hart.overwatch.review.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReviewRequest {

    @NotNull
    private Long authorId;

    @NotNull
    private Long reviewerId;

    @NotNull
    private Byte rating;

    @Size(min = 1, max = 400, message = "A review must be between 1 and 400 characters")
    private String review;


    public CreateReviewRequest() {

    }

    public CreateReviewRequest(Long authorId, Long reviewerId, Byte rating, String review) {
        this.authorId = authorId;
        this.reviewerId = reviewerId;
        this.rating = rating;
        this.review = review;
    }

    public Byte getRating() {
        return rating;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getReview() {
        return review;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }
}

