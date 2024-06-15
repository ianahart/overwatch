package com.hart.overwatch.review.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.review.dto.ReviewDto;

public class GetReviewsResponse {

    private String message;

    private PaginationDto<ReviewDto> data;


    public GetReviewsResponse() {

    }

    public GetReviewsResponse(String message, PaginationDto<ReviewDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ReviewDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ReviewDto> data) {
        this.data = data;
    }
}
