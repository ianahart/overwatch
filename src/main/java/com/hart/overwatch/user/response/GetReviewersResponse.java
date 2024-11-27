package com.hart.overwatch.user.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.user.dto.ReviewerDto;

public class GetReviewersResponse {

    private String message;

    private PaginationDto<ReviewerDto> data;

    public GetReviewersResponse() {

    }

    public GetReviewersResponse(String message, PaginationDto<ReviewerDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ReviewerDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ReviewerDto> data) {
        this.data = data;
    }
}
