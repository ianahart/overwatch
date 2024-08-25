package com.hart.overwatch.todolist.response;

import com.hart.overwatch.todolist.dto.TodoListDto;

public class UpdateTodoListResponse {

    private String message;

    private TodoListDto data;


    public UpdateTodoListResponse() {

    }

    public UpdateTodoListResponse(String message, TodoListDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public TodoListDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(TodoListDto data) {
        this.data = data;
    }

}
