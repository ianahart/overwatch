package com.hart.overwatch.todocardmanagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.activity.ActivityService;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.MoveTodoCardRequest;
import com.hart.overwatch.todocard.request.ReorderTodoCardRequest;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;
import com.hart.overwatch.todocard.request.UploadTodoCardPhotoRequest;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.advice.BadRequestException;

@Service
public class TodoCardManagementService {


    private final TodoListService todoListService;

    private final TodoCardService todoCardService;

    private final ActivityService activityService;

    @Autowired
    public TodoCardManagementService(TodoListService todoListService,
            TodoCardService todoCardService, ActivityService activityService) {
        this.todoListService = todoListService;
        this.todoCardService = todoCardService;
        this.activityService = activityService;
    }

    public TodoCardDto handleCreateTodoCard(Long todoListId, CreateTodoCardRequest request) {
        return todoCardService.createTodoCard(todoListId, request);
    }

    public TodoCardDto handleUpdateTodoCard(Long todoCardId, UpdateTodoCardRequest request) {
        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);
        List<String> updatedProperties =
                PropertyChangeLogger.getUpdatedProperties(todoCard, request);
        String joinedProperties = PropertyChangeLogger.joinProperties(updatedProperties);
        String text = String.format("You updated the following properties: %s to card %s",
                joinedProperties, todoCard.getTitle());

        activityService.createActivity(text, todoCardId, todoCard.getUser().getId());
        return todoCardService.updateTodoCard(todoCard, request);
    }

    public void handleDeleteTodoCard(Long todoCardId) {
        todoCardService.deleteTodoCard(todoCardId);
    }

    public void handleReorderTodoCards(ReorderTodoCardRequest request, Long todoCardId) {
        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);

        if (todoCard == null) {
            throw new BadRequestException("Todo card is missing reordering cards");
        }
        String text = String.format("You moved %s from position %d to position %d",
                todoCard.getTitle(), request.getOldIndex(), request.getNewIndex());
        activityService.createActivity(text, todoCardId, todoCard.getUser().getId());
        todoCardService.reorderTodoCards(request, todoCardId);
    }

    public void handleMoveTodoCards(Long todoCardId, MoveTodoCardRequest request) {
        TodoList destinationList = todoListService.getTodoListById(request.getDestinationListId());
        TodoList sourceList = todoListService.getTodoListById(request.getSourceListId());
        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);

        String text = String.format("Moved card from %s list to %s list", sourceList.getTitle(),
                destinationList.getTitle());
        activityService.createActivity(text, todoCardId, todoCard.getUser().getId());
        todoCardService.moveTodoCards(todoCardId, request, destinationList, sourceList);
    }

    public TodoCardDto handleUploadTodoCardPhoto(Long todoCardId,
            UploadTodoCardPhotoRequest request) {
        TodoCard todoCard = todoCardService.getTodoCardById(todoCardId);
        String text = String.format("You uploaded the a photo called %s to this card",
                request.getFile().getOriginalFilename());
        activityService.createActivity(text, todoCardId, todoCard.getUser().getId());
        return todoCardService.uploadTodoCardPhoto(todoCard, request);
    }
}
