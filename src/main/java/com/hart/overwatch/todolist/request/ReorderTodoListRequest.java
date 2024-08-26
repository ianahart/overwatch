package com.hart.overwatch.todolist.request;

import java.util.List;
import com.hart.overwatch.todolist.dto.TodoListDto;

public class ReorderTodoListRequest {
    private List<TodoListDto> todoLists;


    public ReorderTodoListRequest() {

    }

    public ReorderTodoListRequest(List<TodoListDto> todoLists) {
        this.todoLists = todoLists;
    }

    public List<TodoListDto> getTodoLists() {
        return todoLists;
    }

    public void setTodoLists(List<TodoListDto> todoLists) {
        this.todoLists = todoLists;
    }
}
