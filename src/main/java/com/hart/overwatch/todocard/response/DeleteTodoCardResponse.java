package com.hart.overwatch.todocard.response;

public class DeleteTodoCardResponse {

    private String message;

    public DeleteTodoCardResponse() {

    }

    public DeleteTodoCardResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
