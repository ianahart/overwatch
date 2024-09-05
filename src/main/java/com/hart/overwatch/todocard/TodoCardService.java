package com.hart.overwatch.todocard;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.MoveTodoCardRequest;
import com.hart.overwatch.todocard.request.ReorderTodoCardRequest;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;

@Service
public class TodoCardService {

    private final long MAX_TODO_CARDS_QUANTITY = 10L;

    private final TodoCardRepository todoCardRepository;

    private final UserService userService;

    private final TodoListService todoListService;

    private final TodoListRepository todoListRepository;

    @Autowired
    public TodoCardService(TodoCardRepository todoCardRepository, UserService userService,
            TodoListService todoListService, TodoListRepository todoListRepository) {
        this.todoCardRepository = todoCardRepository;
        this.userService = userService;
        this.todoListService = todoListService;
        this.todoListRepository = todoListRepository;
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



    private List<TodoCardDto> sortTodoCardDtos(List<TodoCardDto> todoCards) {
        return todoCards.stream().sorted(Comparator.comparingInt(TodoCardDto::getIndex))
                .collect(Collectors.toList());
    }


    public List<TodoCardDto> retrieveTodoCards(Long todoListId) {
        User currentUser = userService.getCurrentlyLoggedInUser();

        return sortTodoCardDtos(
                todoCardRepository.retrieveTodoCards(todoListId, currentUser.getId()));
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

    @Transactional
    public void deleteTodoCard(Long todoCardId) {
        TodoCard todoCard = getTodoCardById(todoCardId);
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (todoCard.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("You cannot delete another user's card");
        }

        TodoList todoList = todoCard.getTodoList();
        List<TodoCard> todoCards = sortTodoCards(todoList.getTodoCards());

        if (!todoCards.remove(todoCard)) {
            throw new NotFoundException("TodoCard not found in the list");
        }
        todoList.setTodoCards(todoCards);
        updateCardIndices(todoCards);

        todoListRepository.save(todoList);

        todoCardRepository.delete(todoCard);
    }

    @Transactional
    public void reorderTodoCards(ReorderTodoCardRequest request, Long todoCardId) {

        TodoList todoList = todoListService.getTodoListById(request.getTodoListId());
        List<TodoCard> todoCards = todoList.getTodoCards();

        TodoCard targetCard = todoCards.stream().filter(card -> card.getId().equals(todoCardId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("TodoCard not found"));

        int oldIndex = targetCard.getIndex();
        int newIndex = request.getNewIndex();

        if (oldIndex == newIndex) {
            return;
        }

        for (TodoCard card : todoCards) {
            if (oldIndex < newIndex) {
                if (card.getIndex() > oldIndex && card.getIndex() <= newIndex) {
                    card.setIndex(card.getIndex() - 1);
                }
            } else {
                if (card.getIndex() < oldIndex && card.getIndex() >= newIndex) {
                    card.setIndex(card.getIndex() + 1);
                }
            }
        }

        targetCard.setIndex(newIndex);

        todoCardRepository.saveAll(todoCards);
    }

    @Transactional
    public void moveTodoCards(Long todoCardId, MoveTodoCardRequest request) {
        TodoList destinationList = todoListService.getTodoListById(request.getDestinationListId());
        TodoList sourceList = todoListService.getTodoListById(request.getSourceListId());

        if (destinationList == null) {
            throw new NotFoundException("Missing destination list");
        }
        if (sourceList == null) {
            throw new NotFoundException("Missing source list");
        }

        TodoCard todoCardToMove = getTodoCardById(todoCardId);

        List<TodoCard> sourceTodoCards = sortTodoCards(new ArrayList<>(sourceList.getTodoCards()));
        List<TodoCard> destTodoCards =
                sortTodoCards(new ArrayList<>(destinationList.getTodoCards()));

        if (!sourceTodoCards.remove(todoCardToMove)) {
            throw new NotFoundException("TodoCard not found in source list");
        }

        int newIndex = Math.max(0, Math.min(request.getNewIndex(), destTodoCards.size()));
        todoCardToMove.setTodoList(destinationList);
        destTodoCards.add(newIndex, todoCardToMove);

        updateCardIndices(sourceTodoCards);
        updateCardIndices(destTodoCards);

        sourceList.setTodoCards(sourceTodoCards);
        destinationList.setTodoCards(destTodoCards);

        todoListRepository.save(sourceList);
        todoListRepository.save(destinationList);
    }

    private List<TodoCard> sortTodoCards(List<TodoCard> todoCards) {
        return todoCards.stream().sorted(Comparator.comparingInt(TodoCard::getIndex))
                .collect(Collectors.toList());
    }

    private void updateCardIndices(List<TodoCard> todoCards) {
        for (int i = 0; i < todoCards.size(); i++) {
            TodoCard card = todoCards.get(i);
            card.setIndex(i);
        }
    }

}

