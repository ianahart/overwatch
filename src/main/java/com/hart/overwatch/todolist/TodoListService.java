package com.hart.overwatch.todolist;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.request.UpdateTodoListRequest;

@Service
public class TodoListService {

    private final int MAX_TODO_LISTS = 10;

    private final TodoListRepository todoListRepository;

    private final WorkSpaceService workSpaceService;

    private final UserService userService;

    private final TodoCardService todoCardService;

    @Autowired
    public TodoListService(TodoListRepository todoListRepository, WorkSpaceService workSpaceService,
            UserService userService, @Lazy TodoCardService todoCardService) {
        this.todoListRepository = todoListRepository;
        this.workSpaceService = workSpaceService;
        this.userService = userService;
        this.todoCardService = todoCardService;
    }

    public TodoList getTodoListById(Long todoListId) {
        return todoListRepository.findById(todoListId).orElseThrow(() -> new NotFoundException(
                String.format("A todo list with the id %d was not found", todoListId)));
    }

    private boolean canAddTodoListByQuantity(Long workSpaceId, Long userId) {
        long quantity = todoListRepository.countTodoListsInWorkSpace(workSpaceId, userId);

        return quantity >= MAX_TODO_LISTS ? false : true;
    }

    private boolean todoListAlreadyExists(Long workSpaceId, Long userId, String title) {
        return todoListRepository.findTodoListByWorkSpaceAndUserAndTitle(workSpaceId, userId,
                title.toLowerCase());
    }

    public TodoListDto createTodoList(CreateTodoListRequest request, Long workSpaceId) {

        String cleanedTitle = Jsoup.clean(request.getTitle(), Safelist.none());

        if (!canAddTodoListByQuantity(workSpaceId, request.getUserId())) {
            throw new BadRequestException(
                    "You have reached the maximum amount of lists for this workspace");
        }

        if (todoListAlreadyExists(workSpaceId, request.getUserId(), cleanedTitle)) {
            throw new BadRequestException(
                    String.format("You already have a list %s in this workspace", cleanedTitle));
        }

        User user = userService.getUserById(request.getUserId());
        WorkSpace workSpace = workSpaceService.getWorkSpaceById(workSpaceId);


        TodoList todoList = new TodoList(user, workSpace, cleanedTitle, request.getIndex());

        todoListRepository.save(todoList);

        TodoListDto todoListDto = new TodoListDto(todoList.getId(), todoList.getUser().getId(),
                todoList.getWorkSpace().getId(), todoList.getTitle(), todoList.getIndex(),
                todoList.getCreatedAt());

        List<TodoCardDto> cards = new ArrayList<>();
        todoListDto.setCards(cards);

        return todoListDto;
    }

    private List<TodoListDto> sortTodoLists(Page<TodoListDto> todoLists) {
        return todoLists.getContent().stream()
                .sorted(Comparator.comparingInt(TodoListDto::getIndex))
                .collect(Collectors.toList());
    }

    private List<TodoListDto> attachTodoCards(List<TodoListDto> todoLists) {

        return todoLists.stream().map(tl -> {
            List<TodoCardDto> cards = todoCardService.retrieveTodoCards(tl.getId());
            tl.setCards(cards);
            return tl;
        }).toList();
    }

    public List<TodoListDto> getTodoListsByWorkSpace(Long workSpaceId) {

        if (!workSpaceService.workSpaceExists(workSpaceId)) {
            throw new NotFoundException(String.format(
                    "No workspace exists for id %d. Cannot fetch non existent todo lists",
                    workSpaceId));
        }

        Pageable pageable = PageRequest.of(0, MAX_TODO_LISTS);

        Page<TodoListDto> page = todoListRepository.getTodoListsByWorkSpace(pageable, workSpaceId);

        List<TodoListDto> sortedTodoLists = sortTodoLists(page);
        sortedTodoLists = attachTodoCards(sortedTodoLists);

        return sortedTodoLists;
    }

    public TodoListDto updateTodoList(Long todoListId, UpdateTodoListRequest request) {
        String cleanedTitle = Jsoup.clean(request.getTitle(), Safelist.none());
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (todoListAlreadyExists(request.getWorkSpaceId(), currentUser.getId(), cleanedTitle)) {
            throw new BadRequestException(
                    String.format("You already have a list %s in this workspace", cleanedTitle));
        }

        TodoList todoList = getTodoListById(todoListId);

        if (currentUser.getId() != todoList.getUser().getId()) {
            throw new ForbiddenException("Cannot edit a todo list that is not yours");
        }

        todoList.setTitle(cleanedTitle);
        todoList.setIndex(request.getIndex());

        todoListRepository.save(todoList);

        TodoListDto todoListDto = new TodoListDto(todoList.getId(), todoList.getUser().getId(),
                todoList.getWorkSpace().getId(), todoList.getTitle(), todoList.getIndex(),
                todoList.getCreatedAt());
        List<TodoCardDto> cards = todoCardService.retrieveTodoCards(todoList.getId());
        todoListDto.setCards(cards);

        return todoListDto;


    }

    public void deleteTodoList(Long todoListId) {
        TodoList todoList = getTodoListById(todoListId);
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (todoList.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot delete a todo list that is not yours");
        }
        todoListRepository.delete(todoList);
    }

    public List<TodoListDto> reorderTodoLists(Long workSpaceId, List<TodoListDto> todoListDtos) {
        List<TodoList> todoLists = new ArrayList<>();

        for (int i = 0; i < todoListDtos.size(); i++) {
            Long todoListDtoId = todoListDtos.get(i).getId();
            TodoList todoList = getTodoListById(todoListDtoId);
            todoList.setIndex(i);
            todoLists.add(todoList);

        }

        todoListRepository.saveAll(todoLists);

        List<TodoListDto> result = getTodoListsByWorkSpace(workSpaceId);
        result = attachTodoCards(result);

        return result;
    }
}
