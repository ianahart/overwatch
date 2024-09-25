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
import com.hart.overwatch.activity.dto.ActivityDto;
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
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;
import com.hart.overwatch.todocard.request.UploadTodoCardPhotoRequest;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
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


    @Test
    public void TodoCardManagementService_HandleUpdateTodoCard_ReturnTodoCardDto()
            throws Exception {
        TodoCard todoCard = todoList.getTodoCards().get(0);
        LocalDateTime timestamp = LocalDateTime.now();
        when(todoCardService.getTodoCardById(todoCard.getId())).thenReturn(todoCard);

        UpdateTodoCardRequest request = new UpdateTodoCardRequest();
        request.setId(1L);
        request.setColor("#000000");
        request.setLabel("label");
        request.setPhoto("photo");
        request.setTitle("title");
        request.setIndex(1);
        request.setDetails("details");
        request.setUploadPhotoUrl("photo_url");
        request.setEndDate(timestamp);
        request.setStartDate(timestamp);
        request.setCreatedAt(timestamp);

        TodoCardDto todoCardDto = new TodoCardDto();
        todoCardDto.setId(1L);
        todoCardDto.setIndex(request.getIndex());
        todoCardDto.setTitle(request.getTitle());


        String text =
                "You updated the following properties: Title, Details, Label, Color, Start Date, End Date, Photo, Uploaded photo to card title-1";
        when(activityService.createActivity(text, todoCard.getId(), user.getId()))
                .thenReturn(new ActivityDto(1L, 1L, 1L, timestamp, text, "avatar_url"));
        when(todoCardService.updateTodoCard(todoCard, request)).thenReturn(todoCardDto);

        TodoCardDto returnedTodoCardDto =
                todoCardManagementService.handleUpdateTodoCard(todoCard.getId(), request);

        Assertions.assertThat(returnedTodoCardDto).isNotNull();
    }

    @Test
    public void TodoCardManagementService_HandleDeleteTodoCard_ReturnNothing() {
        Long todoCardId = todoList.getTodoCards().get(0).getId();
        doNothing().when(todoCardService).deleteTodoCard(todoCardId);
        todoCardManagementService.handleDeleteTodoCard(todoCardId);

        verify(todoCardService, times(1)).deleteTodoCard(todoCardId);

    }

    @Test
    public void TodoCardManagementService_HandleReorderTodoCards_ThrowBadRequestException() {
        TodoCard todoCard = todoList.getTodoCards().get(0);
        when(todoCardService.getTodoCardById(todoCard.getId())).thenReturn(null);
        ReorderTodoCardRequest request = new ReorderTodoCardRequest(todoList.getId(), 2, 1);

        Assertions.assertThatThrownBy(() -> {
            todoCardManagementService.handleReorderTodoCards(request, todoCard.getId());
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Todo card is missing reordering cards");
    }

    @Test
    public void TodoCardManagementService_HandleReorderTodoCards_ReturnNothing() {
        TodoCard todoCard = todoList.getTodoCards().get(0);
        LocalDateTime timestamp = LocalDateTime.now();
        when(todoCardService.getTodoCardById(todoCard.getId())).thenReturn(todoCard);

        ReorderTodoCardRequest request = new ReorderTodoCardRequest(todoList.getId(), 2, 1);
        String text = String.format("You moved %s from position %d to position %d",
                todoCard.getTitle(), request.getOldIndex(), request.getNewIndex());
        when(activityService.createActivity(text, todoCard.getId(), user.getId()))
                .thenReturn(new ActivityDto(1L, 1L, 1L, timestamp, text, "avatar_url"));

        doNothing().when(todoCardService).reorderTodoCards(request, todoCard.getId());

        todoCardManagementService.handleReorderTodoCards(request, todoCard.getId());

        verify(todoCardService, times(1)).reorderTodoCards(request, todoCard.getId());
    }
}


