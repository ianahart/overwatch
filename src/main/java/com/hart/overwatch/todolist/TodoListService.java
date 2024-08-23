package com.hart.overwatch.todolist;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;

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
}
