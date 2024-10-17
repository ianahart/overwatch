package com.hart.overwatch.blockuser.response;

import com.hart.overwatch.blockuser.dto.BlockUserDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllBlockUserResponse {

    private String message;

    private PaginationDto<BlockUserDto> data;

    public GetAllBlockUserResponse() {

    }

    public GetAllBlockUserResponse(String message, PaginationDto<BlockUserDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<BlockUserDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<BlockUserDto> data) {
        this.data = data;
    }
}
