package com.hart.overwatch.todocard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;
import com.hart.overwatch.todocard.response.CreateTodoCardResponse;
import com.hart.overwatch.todocard.response.UpdateTodoCardResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class TodoCardController {

    private final TodoCardService todoCardService;

    @Autowired
    public TodoCardController(TodoCardService todoCardService) {
        this.todoCardService = todoCardService;
    }

    @PostMapping("/todo-lists/{todoListId}/todo-cards")
    ResponseEntity<CreateTodoCardResponse> createTodoCard(
            @PathVariable("todoListId") Long todoListId,
            @Valid @RequestBody CreateTodoCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTodoCardResponse("success",
                todoCardService.createTodoCard(todoListId, request)));
    }

    @PutMapping("/todo-cards/{todoCardId}")
    ResponseEntity<UpdateTodoCardResponse> updateTodoCard(
            @PathVariable("todoCardId") Long todoCardId,
            @Valid @RequestBody UpdateTodoCardRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(new UpdateTodoCardResponse("success",
                todoCardService.updateTodoCard(todoCardId, request)));
    }
}
