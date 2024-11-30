package com.hart.overwatch.teamcomment.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.teamcomment.dto.TeamCommentDto;

public class GetTeamCommentsResponse {

    private String message;

    private PaginationDto<TeamCommentDto> data;

    public GetTeamCommentsResponse() {}

    public GetTeamCommentsResponse(String message, PaginationDto<TeamCommentDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TeamCommentDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TeamCommentDto> data) {
        this.data = data;
    }
}
