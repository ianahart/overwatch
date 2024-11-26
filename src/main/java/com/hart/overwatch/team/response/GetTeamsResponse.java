package com.hart.overwatch.team.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.team.dto.TeamDto;

public class GetTeamsResponse {

    private String message;

    private PaginationDto<TeamDto> data;

    public GetTeamsResponse() {

    }

    public GetTeamsResponse(String message, PaginationDto<TeamDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TeamDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TeamDto> data) {
        this.data = data;
    }
}
