package com.hart.overwatch.todocard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.MoveTodoCardRequest;
import com.hart.overwatch.todocard.request.ReorderTodoCardRequest;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;
import com.hart.overwatch.todocard.request.UploadTodoCardPhotoRequest;
import com.hart.overwatch.todocard.response.CreateTodoCardResponse;
import com.hart.overwatch.todocard.response.DeleteTodoCardResponse;
import com.hart.overwatch.todocard.response.MoveTodoCardResponse;
import com.hart.overwatch.todocard.response.ReorderTodoCardResponse;
import com.hart.overwatch.todocard.response.UpdateTodoCardResponse;
import com.hart.overwatch.todocard.response.UploadTodoCardPhotoResponse;
import com.hart.overwatch.todocardmanagement.TodoCardManagementService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class TodoCardController {


    private final TodoCardManagementService todoCardManagementService;

    @Autowired
    public TodoCardController(TodoCardManagementService todoCardManagementService) {
        this.todoCardManagementService = todoCardManagementService;
    }

    @PostMapping("/todo-lists/{todoListId}/todo-cards")
    ResponseEntity<CreateTodoCardResponse> createTodoCard(
            @PathVariable("todoListId") Long todoListId,
            @Valid @RequestBody CreateTodoCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTodoCardResponse("success",
                todoCardManagementService.handleCreateTodoCard(todoListId, request)));
    }

    @PutMapping("/todo-cards/{todoCardId}")
    ResponseEntity<UpdateTodoCardResponse> updateTodoCard(
            @PathVariable("todoCardId") Long todoCardId,
            @Valid @RequestBody UpdateTodoCardRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(new UpdateTodoCardResponse("success",
                todoCardManagementService.handleUpdateTodoCard(todoCardId, request)));
    }

    @DeleteMapping("/todo-cards/{todoCardId}")
    ResponseEntity<DeleteTodoCardResponse> deleteTodoCard(
            @PathVariable("todoCardId") Long todoCardId) {
        todoCardManagementService.handleDeleteTodoCard(todoCardId);

        return ResponseEntity.status(HttpStatus.OK).body(new DeleteTodoCardResponse("success"));
    }

    @PatchMapping("/todo-cards/{todoCardId}/reorder")
    ResponseEntity<ReorderTodoCardResponse> reorderTodoCard(
            @PathVariable("todoCardId") Long todoCardId,
            @RequestBody ReorderTodoCardRequest request) {
        todoCardManagementService.handleReorderTodoCards(request, todoCardId);
        return ResponseEntity.status(HttpStatus.OK).body(new ReorderTodoCardResponse("success"));
    }

    @PatchMapping("/todo-cards/{todoCardId}/move")
    ResponseEntity<MoveTodoCardResponse> moveTodoCard(@PathVariable("todoCardId") Long todoCardId,
            @RequestBody MoveTodoCardRequest request) {
        todoCardManagementService.handleMoveTodoCards(todoCardId, request);

        return ResponseEntity.status(HttpStatus.OK).body(new MoveTodoCardResponse("success"));
    }

    @PatchMapping("/todo-cards/{todoCardId}/upload")
    ResponseEntity<UploadTodoCardPhotoResponse> uploadTodoCardPhoto(
            @PathVariable("todoCardId") Long todoCardId, UploadTodoCardPhotoRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UploadTodoCardPhotoResponse("success",
                todoCardManagementService.handleUploadTodoCardPhoto(todoCardId, request)));
    }
}
