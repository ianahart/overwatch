package com.hart.overwatch.teamcomment.response;

import com.hart.overwatch.teamcomment.dto.MinTeamCommentDto;

public class UpdateOrDeleteTeamCommentResponse {

    private String message;

    private MinTeamCommentDto data;

    public UpdateOrDeleteTeamCommentResponse() {

    }

    public UpdateOrDeleteTeamCommentResponse(String message, MinTeamCommentDto data) {
        this.message = message;
        this.data = data;
    }

    public MinTeamCommentDto getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(MinTeamCommentDto data) {
        this.data = data;
    }
}
