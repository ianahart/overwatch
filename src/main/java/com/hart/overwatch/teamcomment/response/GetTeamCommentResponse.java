package com.hart.overwatch.teamcomment.response;

import com.hart.overwatch.teamcomment.dto.MinTeamCommentDto;

public class GetTeamCommentResponse {

    private String message;

    public MinTeamCommentDto data;


    public GetTeamCommentResponse() {

    }

    public GetTeamCommentResponse(String message, MinTeamCommentDto data) {
        this.message = message;
        this.data = data;
    }

    public MinTeamCommentDto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(MinTeamCommentDto data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
