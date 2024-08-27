package com.hart.overwatch.todocard.response;

public class CreateTodoCardResponse {

    private String message;


    public CreateTodoCardResponse() {

    }

    public CreateTodoCardResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

