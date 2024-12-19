package com.hart.overwatch.badge.response;

import com.hart.overwatch.badge.dto.BadgeDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllBadgesResponse {

    private String message;

    private PaginationDto<BadgeDto> data;

    public GetAllBadgesResponse() {

    }

    public GetAllBadgesResponse(String message, PaginationDto<BadgeDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<BadgeDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<BadgeDto> data) {
        this.data = data;
    }
}
