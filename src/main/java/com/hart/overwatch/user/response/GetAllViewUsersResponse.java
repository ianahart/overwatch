package com.hart.overwatch.user.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.user.dto.ViewUserDto;

public class GetAllViewUsersResponse {

    private String message;

    private PaginationDto<ViewUserDto> data;

    public GetAllViewUsersResponse() {

    }

    public GetAllViewUsersResponse(String message, PaginationDto<ViewUserDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ViewUserDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ViewUserDto> data) {
        this.data = data;
    }
}
