package com.hart.overwatch.teamcomment.response;

public class GetTeamCommentResponse {

    private String message;

    public String data;


    public GetTeamCommentResponse() {

    }

    public GetTeamCommentResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
