package com.hart.overwatch.reviewerbadge.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.reviewerbadge.dto.ReviewerBadgeDto;

public class GetAllReviewerBadgesResponse {

    private String message;

    private PaginationDto<ReviewerBadgeDto> data;

    public GetAllReviewerBadgesResponse() {

    }

    public GetAllReviewerBadgesResponse(String message, PaginationDto<ReviewerBadgeDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ReviewerBadgeDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ReviewerBadgeDto> data) {
        this.data = data;
    }
}
