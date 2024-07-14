package com.hart.overwatch.repository.response;

public class CreateUserRepositoryResponse {

    private String message;


    public CreateUserRepositoryResponse() {

    }

    public CreateUserRepositoryResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
