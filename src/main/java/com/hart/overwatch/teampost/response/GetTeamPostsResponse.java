package com.hart.overwatch.teampost.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teampost.dto.TeamPostDto;

public class GetTeamPostsResponse {

    private String message;

    private PaginationDto<TeamPostDto> data;

    public GetTeamPostsResponse() {

    }

    public GetTeamPostsResponse(String message, PaginationDto<TeamPostDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TeamPostDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TeamPostDto> data) {
        this.data = data;
    }
}
