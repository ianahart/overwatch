package com.hart.overwatch.connection.response;

import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllConnectionsResponse {

    private String message;

    private PaginationDto<ConnectionDto> data;

    public GetAllConnectionsResponse() {

    }

    public GetAllConnectionsResponse(String message, PaginationDto<ConnectionDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<ConnectionDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<ConnectionDto> data) {
        this.data = data;
    }

}
