package com.hart.overwatch.todolist.response;


public class DeleteTodoListResponse {

    private String message;

    public DeleteTodoListResponse() {

    }

    public DeleteTodoListResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
