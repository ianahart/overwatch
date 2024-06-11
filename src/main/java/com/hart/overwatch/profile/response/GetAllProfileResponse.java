package com.hart.overwatch.profile.response;


import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.dto.AllProfileDto;

public class GetAllProfileResponse {

    private String message;
    private PaginationDto<AllProfileDto> data;


    public GetAllProfileResponse() {

    }


    public GetAllProfileResponse(String message, PaginationDto<AllProfileDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<AllProfileDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<AllProfileDto> data) {
        this.data = data;
    }
}
