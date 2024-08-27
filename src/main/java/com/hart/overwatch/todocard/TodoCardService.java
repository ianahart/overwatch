package com.hart.overwatch.todocard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;

@Service
public class TodoCardService {

    private final TodoCardRepository todoCardRepository;

    private final UserService userService;

    private final TodoListService todoListService;

    @Autowired
    public TodoCardService(TodoCardRepository todoCardRepository, UserService userService,
            TodoListService todoListService) {
        this.todoCardRepository = todoCardRepository;
        this.userService = userService;
        this.todoListService = todoListService;
    }


    private TodoCard getTodoCardById(Long todoCardId) {
        return todoCardRepository.findById(todoCardId).orElseThrow(() -> new NotFoundException(
                String.format("A todo card with the id %d was not found", todoCardId)));
    }

    public void createTodoCard(Long todoListId, CreateTodoCardRequest request) {
        System.out.println();
        System.out.println(todoListId);
        System.out.println("Creating a todo card");
        System.out.println();
    }

}
