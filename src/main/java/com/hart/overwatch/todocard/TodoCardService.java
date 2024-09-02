package com.hart.overwatch.todocard;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;

@Service
public class TodoCardService {

    private final long MAX_TODO_CARDS_QUANTITY = 10L;

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

    private boolean cannotAddTodoCardByQuantity(Long todoListId, Long userId) {
        long quantity = todoCardRepository.countTodoCardsInTodoList(todoListId, userId);

        return quantity >= MAX_TODO_CARDS_QUANTITY;

    }

    private boolean todoCardAlreadyExistsInList(Long todoListId, String title) {
        TodoList todoList = todoListService.getTodoListById(todoListId);
        int countOfDuplicates = todoList.getTodoCards().stream()
                .filter((v) -> v.getTitle().equals(title.toLowerCase())).toList().size();
        return countOfDuplicates >= 1;
    }

    public TodoCardDto createTodoCard(Long todoListId, CreateTodoCardRequest request) {

        String cleanedTitle = Jsoup.clean(request.getTitle(), Safelist.none());

        if (cannotAddTodoCardByQuantity(todoListId, request.getUserId())) {
            throw new BadRequestException(
                    String.format("You have added the maximum amount of cards (%d) for this list",
                            MAX_TODO_CARDS_QUANTITY));
        }

        if (todoCardAlreadyExistsInList(todoListId, cleanedTitle)) {
            throw new BadRequestException(
                    "You have already added a card with that title to this list");
        }

        User user = userService.getUserById(request.getUserId());
        TodoList todoList = todoListService.getTodoListById(todoListId);

        TodoCard todoCard = new TodoCard(cleanedTitle, request.getIndex(), user, todoList);

        todoCardRepository.save(todoCard);

        return new TodoCardDto(todoCard.getId(), todoCard.getTodoList().getId(),
                todoCard.getUser().getId(), todoCard.getCreatedAt(), todoCard.getLabel(),
                todoCard.getTitle(), todoCard.getColor(), todoCard.getIndex(),
                todoCard.getDetails(), todoCard.getStartDate(), todoCard.getEndDate(),
                todoCard.getPhoto(), todoCard.getTodoList().getTitle());
    }

    public List<TodoCardDto> retrieveTodoCards(Long todoListId) {
        User currentUser = userService.getCurrentlyLoggedInUser();

        return todoCardRepository.retrieveTodoCards(todoListId, currentUser.getId());
    }

    public TodoCardDto retrieveTodoCard(Long todoListId) {
        User currentUser = userService.getCurrentlyLoggedInUser();

        return todoCardRepository.retrieveTodoCard(todoListId, currentUser.getId());
    }

    public TodoCardDto updateTodoCard(Long todoCardId, UpdateTodoCardRequest request) {
        String cleanedTitle =
                request.getTitle() != null ? Jsoup.clean(request.getTitle(), Safelist.none()) : "";
        TodoCard todoCard = getTodoCardById(todoCardId);


        if (todoCardAlreadyExistsInList(todoCard.getTodoList().getId(), cleanedTitle)) {
            throw new BadRequestException(
                    "You have already added a card with that title to this list");
        }


        String cleanedDetails =
                request.getDetails() != null ? Jsoup.clean(request.getDetails(), Safelist.none())
                        : null;
        String cleanedLabel =
                request.getLabel() != null ? Jsoup.clean(request.getLabel(), Safelist.none())
                        : null;

        todoCard.setLabel(cleanedLabel);
        todoCard.setTitle(cleanedTitle);
        todoCard.setColor(request.getColor());
        todoCard.setIndex(request.getIndex());
        todoCard.setDetails(cleanedDetails);
        todoCard.setStartDate(request.getStartDate());
        todoCard.setEndDate(request.getEndDate());
        todoCard.setPhoto(request.getPhoto());

        todoCardRepository.save(todoCard);

        return new TodoCardDto(todoCard.getId(), todoCard.getTodoList().getId(),
                todoCard.getUser().getId(), todoCard.getCreatedAt(), todoCard.getLabel(),
                todoCard.getTitle(), todoCard.getColor(), todoCard.getIndex(),
                todoCard.getDetails(), todoCard.getStartDate(), todoCard.getEndDate(),
                todoCard.getPhoto(), todoCard.getTodoList().getTitle());

    }

}
