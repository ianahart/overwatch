package com.hart.overwatch.todocard.response;

public class ReorderTodoCardResponse {

    private String message;


    public ReorderTodoCardResponse() {

    }

    public ReorderTodoCardResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
