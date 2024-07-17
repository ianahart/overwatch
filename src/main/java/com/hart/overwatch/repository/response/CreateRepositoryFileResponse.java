package com.hart.overwatch.repository.response;

public class CreateRepositoryFileResponse {

    private String message;

    private String data;

    public CreateRepositoryFileResponse() {

    }

    public CreateRepositoryFileResponse(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
