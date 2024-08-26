package com.hart.overwatch.todolist;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.request.UpdateTodoListRequest;

@Service
public class TodoListService {

    private final int MAX_TODO_LISTS = 10;

    private final TodoListRepository todoListRepository;

    private final WorkSpaceService workSpaceService;

    private final UserService userService;

    @Autowired
    public TodoListService(TodoListRepository todoListRepository, WorkSpaceService workSpaceService,
            UserService userService) {
        this.todoListRepository = todoListRepository;
        this.workSpaceService = workSpaceService;
        this.userService = userService;
    }

    private TodoList getTodoListById(Long todoListId) {
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

        return new TodoListDto(todoList.getId(), todoList.getUser().getId(),
                todoList.getWorkSpace().getId(), todoList.getTitle(), todoList.getIndex(),
                todoList.getCreatedAt());
    }

    private List<TodoListDto> sortTodoLists(Page<TodoListDto> todoLists) {
        return todoLists.getContent().stream()
                .sorted(Comparator.comparingInt(TodoListDto::getIndex))
                .collect(Collectors.toList());
    }

    public List<TodoListDto> getTodoListsByWorkSpace(Long workSpaceId) {

        if (!workSpaceService.workSpaceExists(workSpaceId)) {
            throw new NotFoundException(String.format(
                    "No workspace exists for id %d. Cannot fetch non existent todo lists",
                    workSpaceId));
        }

        Pageable pageable = PageRequest.of(0, MAX_TODO_LISTS);

        Page<TodoListDto> page = todoListRepository.getTodoListsByWorkSpace(pageable, workSpaceId);

        return sortTodoLists(page);
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

        return new TodoListDto(todoList.getId(), todoList.getUser().getId(),
                todoList.getWorkSpace().getId(), todoList.getTitle(), todoList.getIndex(),
                todoList.getCreatedAt());

    }

    public void deleteTodoList(Long todoListId) {
        TodoList todoList = getTodoListById(todoListId);
        User currentUser = userService.getCurrentlyLoggedInUser();

        if (todoList.getUser().getId() != currentUser.getId()) {
            throw new ForbiddenException("Cannot delete a todo list that is not yours");
        }
        todoListRepository.delete(todoList);
    }

}
