package com.hart.overwatch.todocardmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import com.hart.overwatch.activity.ActivityService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.MoveTodoCardRequest;
import com.hart.overwatch.todocard.request.ReorderTodoCardRequest;
import com.hart.overwatch.todocard.request.UploadTodoCardPhotoRequest;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.HashMap;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TodoCardManagementServiceTest {

    @InjectMocks
    private TodoCardManagementService todoCardManagementService;

    @Mock
    TodoListService todoListService;

    @Mock
    TodoCardService todoCardService;

    @Mock
    ActivityService activityService;

    private User user;

    private TodoList todoList;

    private WorkSpace workSpace;

    private List<TodoCard> cards;

    @BeforeEach
    public void init() {
        user = createUser();
        workSpace = createWorkSpace(user);
        todoList = createTodoList(user, workSpace);
        todoList.setId(1L);
        cards = createTodoCards(todoList, user);
        todoList.setTodoCards(cards);

    }

    private List<TodoCard> createTodoCards(TodoList todoList, User user) {
        int cardsToGenerate = 2;
        List<TodoCard> todoCards = new ArrayList<>();
        for (int i = 1; i < cardsToGenerate; i++) {
            TodoCard todoCard = new TodoCard();
            todoCard.setId(Long.valueOf(i));
            todoCard.setIndex(i);
            todoCard.setUser(user);
            todoCard.setTodoList(todoList);
            todoCard.setTitle(String.format("title-%d", i));
            todoCard.setUploadPhotoUrl("https://imgur.com");
            todoCard.setUploadPhotoFileName("sdkfjdsklfjds.jpeg");
            todoCards.add(todoCard);
        }
        return todoCards;
    }

    private TodoList createTodoList(User user, WorkSpace workSpace) {
        TodoList todoList = new TodoList(user, workSpace, "todo list title", 0);
        todoList.setId(1L);

        return todoList;
    }

    private User createUser() {
        Boolean loggedIn = true;
        User user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        user.setId(1L);

        return user;
    }

    private WorkSpace createWorkSpace(User user) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setTitle("main");
        workSpace.setBackgroundColor("#000000");
        workSpace.setUser(user);
        workSpace.setId(1L);

        return workSpace;
    }

    @Test
    public void TodoCardManagementService_HandleCreateTodoCard_ReturnTodoCardDto()
            throws Exception {
        Long todoListId = todoList.getId();
        CreateTodoCardRequest request = new CreateTodoCardRequest("title-3", 2, user.getId());
        TodoCardDto todoCardDto = new TodoCardDto();
        todoCardDto.setId(3L);
        todoCardDto.setIndex(request.getIndex());
        todoCardDto.setTitle(request.getTitle());

        when(todoCardService.createTodoCard(todoListId, request)).thenReturn(todoCardDto);

        TodoCardDto returnedTodoCardDto =
                todoCardManagementService.handleCreateTodoCard(todoListId, request);

        Assertions.assertThat(returnedTodoCardDto).isNotNull();
        Assertions.assertThat(returnedTodoCardDto.getTitle()).isEqualTo(todoCardDto.getTitle());
        Assertions.assertThat(returnedTodoCardDto.getIndex()).isEqualTo(todoCardDto.getIndex());
    }
}


