package com.hart.overwatch.activity.response;

import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class FetchActivityResponse {

    private String message;

    private PaginationDto<ActivityDto> data;

    public FetchActivityResponse() {

    }

    public FetchActivityResponse(String message, PaginationDto<ActivityDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaginationDto<ActivityDto> getData() {
        return data;
    }

    public void setData(PaginationDto<ActivityDto> data) {
        this.data = data;
    }
}
