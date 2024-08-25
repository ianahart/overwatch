package com.hart.overwatch.todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.request.UpdateTodoListRequest;
import com.hart.overwatch.todolist.response.CreateTodoListResponse;
import com.hart.overwatch.todolist.response.GetTodoListsResponse;
import com.hart.overwatch.todolist.response.UpdateTodoListResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class TodoListController {

    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping(path = "/workspaces/{workSpaceId}/todo-lists")
    public ResponseEntity<CreateTodoListResponse> createTodoList(
            @PathVariable("workSpaceId") Long workSpaceId,
            @Valid @RequestBody CreateTodoListRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTodoListResponse("success",
                todoListService.createTodoList(request, workSpaceId)));
    }

    @GetMapping(path = "/workspaces/{workSpaceId}/todo-lists")
    public ResponseEntity<GetTodoListsResponse> getTodoLists(
            @PathVariable("workSpaceId") Long workSpaceId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetTodoListsResponse("success",
                todoListService.getTodoListsByWorkSpace(workSpaceId)));
    }

    @PatchMapping(path = "/todo-lists/{todoListId}")
    public ResponseEntity<UpdateTodoListResponse> updateTodoList(
            @PathVariable("todoListId") Long todoListId,
            @Valid @RequestBody UpdateTodoListRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateTodoListResponse("success",
                todoListService.updateTodoList(todoListId, request)));
    }
}
