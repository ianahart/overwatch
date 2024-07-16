package com.hart.overwatch.repository.response;

public class GetRepositoryCommentResponse {

    private String message;

    private String data;

    public GetRepositoryCommentResponse() {

    }


    public GetRepositoryCommentResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }
}
