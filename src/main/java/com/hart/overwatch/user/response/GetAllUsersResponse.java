package com.hart.overwatch.user.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.user.dto.MinUserDto;

public class GetAllUsersResponse {

    private String message;

    private PaginationDto<MinUserDto> data;

    public GetAllUsersResponse() {

    }

    public GetAllUsersResponse(String message, PaginationDto<MinUserDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<MinUserDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<MinUserDto> data) {
        this.data = data;
    }
}


