package com.hart.overwatch.repository.response;

public class DeleteRepositoryResponse {

    private String message;

    public DeleteRepositoryResponse() {

    }

    public DeleteRepositoryResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
