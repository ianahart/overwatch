package com.hart.overwatch.todocard.response;

public class MoveTodoCardResponse {

    private String message;

    public MoveTodoCardResponse() {

    }

    public MoveTodoCardResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
