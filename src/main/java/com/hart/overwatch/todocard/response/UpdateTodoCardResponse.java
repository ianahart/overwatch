package com.hart.overwatch.todocard.response;

import com.hart.overwatch.todocard.dto.TodoCardDto;

public class UpdateTodoCardResponse {

    private String message;

    private TodoCardDto data;


    public UpdateTodoCardResponse() {

    }

    public UpdateTodoCardResponse(String message, TodoCardDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public TodoCardDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(TodoCardDto data) {
        this.data = data;
    }
}
