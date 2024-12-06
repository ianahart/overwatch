package com.hart.overwatch.ban.response;

import com.hart.overwatch.ban.dto.BanDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllBansResponse {

    private String message;

    private PaginationDto<BanDto> data;

    public GetAllBansResponse() {

    }

    public GetAllBansResponse(String message, PaginationDto<BanDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<BanDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<BanDto> data) {
        this.data = data;
    }
}
