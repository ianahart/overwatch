package com.hart.overwatch.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.response.CreateTodoListResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/workspaces")
public class TodoListController {

    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping("/{workSpaceId}/todo-lists")
    public ResponseEntity<CreateTodoListResponse> createTodoList(
            @PathVariable("workSpaceId") Long workSpaceId,
            @Valid @RequestBody CreateTodoListRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTodoListResponse("success",
                todoListService.createTodoList(request, workSpaceId)));
    }
}
