package com.hart.overwatch.todolist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCardRepository;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
import com.hart.overwatch.workspace.dto.CreateWorkSpaceDto;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.workspace.request.UpdateWorkSpaceRequest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TodoListServiceTest {

    @InjectMocks
    private TodoListService todoListService;

    @Mock
    TodoListRepository todoListRepository;

    @Mock
    private UserService userService;

    @Mock
    private WorkSpaceService workSpaceService;

    @Mock
    private TodoCardService todoCardService;

    @Mock
    private TodoCardRepository todoCardRepository;

    private User user;

    private WorkSpace workSpace;

    private TodoList todoList;


    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        workSpace = new WorkSpace("main", "#000000", user);
        workSpace.setId(1L);
        user.setId(1L);
        workSpace.setUser(user);

        todoList = new TodoList(user, workSpace, "todo list title", 0);

        todoList.setId(1L);
    }

    @Test
    public void TodoListService_GetTodoListById_ThrowNotFoundException() {
        Long nonExistentId = 999L;
        when(todoListRepository.findById(nonExistentId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            todoListService.getTodoListById(nonExistentId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("A todo list with the id %d was not found", nonExistentId));
    }

    @Test
    public void TodoListService_GetTodoListById_ReturnTodoList() {
        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));

        TodoList returnedTodoList = todoListService.getTodoListById(todoList.getId());

        Assertions.assertThat(returnedTodoList).isNotNull();
        Assertions.assertThat(returnedTodoList.getId()).isEqualTo(todoList.getId());
    }

    @Test
    public void TodoListService_CreateTodoList_ThrowBadRequestExceptionMax() {
        CreateTodoListRequest request = new CreateTodoListRequest(null, null, null);
        request.setTitle("todo list title");
        request.setUserId(user.getId());
        request.setIndex(0);

        when(todoListRepository.countTodoListsInWorkSpace(workSpace.getId(), user.getId()))
                .thenReturn(11L);

        Assertions.assertThatThrownBy(() -> {
            todoListService.createTodoList(request, workSpace.getId());
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have reached the maximum amount of lists for this workspace");
    }

    @Test
    public void TodoListService_CreateTodoList_ThrowBadRequestExceptionExists() {
        CreateTodoListRequest request = new CreateTodoListRequest();
        request.setTitle("todo list title");
        request.setUserId(user.getId());
        request.setIndex(0);

        when(todoListRepository.countTodoListsInWorkSpace(workSpace.getId(), user.getId()))
                .thenReturn(1L);
        when(todoListRepository.findTodoListByWorkSpaceAndUserAndTitle(workSpace.getId(),
                user.getId(), request.getTitle())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            todoListService.createTodoList(request, workSpace.getId());
        }).isInstanceOf(BadRequestException.class).hasMessage(
                String.format("You already have a list %s in this workspace", request.getTitle()));
    }

}


