package com.hart.overwatch.review.dto;

public class MinReviewDto {
    private Long id;

    private Byte rating;

    private String review;


    public MinReviewDto() {

    }

    public MinReviewDto(Long id, Byte rating, String review) {
        this.id = id;
        this.rating = rating;
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public Byte getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

}
