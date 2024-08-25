package com.hart.overwatch.todolist.response;

import java.util.List;
import com.hart.overwatch.todolist.dto.TodoListDto;

public class GetTodoListsResponse {

    private String message;

    private List<TodoListDto> data;

    public GetTodoListsResponse() {

    }

    public GetTodoListsResponse(String message, List<TodoListDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<TodoListDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<TodoListDto> data) {
        this.data = data;
    }
}
