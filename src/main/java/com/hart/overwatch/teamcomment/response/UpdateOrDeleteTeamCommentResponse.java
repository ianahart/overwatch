package com.hart.overwatch.teamcomment.response;

public class UpdateOrDeleteTeamCommentResponse {

    private String message;

    private String data;

    public UpdateOrDeleteTeamCommentResponse() {

    }

    public UpdateOrDeleteTeamCommentResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }
}
