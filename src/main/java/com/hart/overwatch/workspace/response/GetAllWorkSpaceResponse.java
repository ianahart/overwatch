package com.hart.overwatch.workspace.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;

public class GetAllWorkSpaceResponse {

    private String message;

    private PaginationDto<WorkSpaceDto> data;


    public GetAllWorkSpaceResponse() {

    }

    public GetAllWorkSpaceResponse(String message, PaginationDto<WorkSpaceDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<WorkSpaceDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<WorkSpaceDto> data) {
        this.data = data;
    }
}

