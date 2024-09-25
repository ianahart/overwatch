package com.hart.overwatch.todocard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardRepository;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todocard.request.UploadTodoCardPhotoRequest;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.todolist.TodoListService;
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.request.UpdateTodoListRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TodoCardServiceTest {

    @InjectMocks
    private TodoCardService todoCardService;

    @Mock
    TodoListService todoListService;

    @Mock
    TodoListRepository todoListRepository;

    @Mock
    private UserService userService;

    @Mock
    private TodoCardRepository todoCardRepository;

    @Mock
    AmazonService amazonService;

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

    private List<TodoCardDto> createTodoCardDtos(TodoList todoList) {
        List<TodoCardDto> todoCardDtos = new ArrayList<>();
        for (TodoCard todoCard : todoList.getTodoCards()) {
            TodoCardDto todoCardDto = new TodoCardDto();
            todoCardDto.setId(todoCard.getId());
            todoCardDto.setIndex(todoCard.getIndex());
            todoCardDto.setUserId(todoCard.getUser().getId());
            todoCardDto.setTodoListId(todoCard.getTodoList().getId());
            todoCardDto.setTitle(todoCard.getTitle());
            todoCardDtos.add(todoCardDto);
        }
        return todoCardDtos;
    }

    private List<TodoListDto> createTodoListDtos(TodoList todoList) {
        TodoListDto todoListDto = new TodoListDto();
        todoListDto.setId(todoList.getId());
        todoListDto.setTitle(todoList.getTitle());
        todoListDto.setIndex(todoList.getIndex());
        todoListDto.setWorkSpaceId(todoList.getWorkSpace().getId());
        todoListDto.setUserId(todoList.getUser().getId());
        todoListDto.setCards(createTodoCardDtos(todoList));

        return List.of(todoListDto);

    }

    private TodoCardDto createTodoCardDto(Long id, TodoList todoList) {
        LocalDateTime createdAt = LocalDateTime.now();
        String title = String.format("%d-title", id);
        Integer index = id.intValue();

        TodoCardDto todoCardDto = new TodoCardDto();

        todoCardDto.setId(id);
        todoCardDto.setColor("#000000");
        todoCardDto.setLabel("label");
        todoCardDto.setPhoto("https://imgur.com/photo");
        todoCardDto.setTitle(title);
        todoCardDto.setIndex(index);
        todoCardDto.setDetails("some details");
        todoCardDto.setTodoListId(todoList.getId());
        todoCardDto.setUserId(user.getId());
        todoCardDto.setEndDate(createdAt);
        todoCardDto.setStartDate(createdAt);
        todoCardDto.setCreatedAt(createdAt);
        todoCardDto.setUploadPhotoUrl("https://imgur.com/photo");
        return todoCardDto;
    }


    private TodoListDto createTodoListDto(TodoList todoList) {
        TodoListDto todoListDto = new TodoListDto();
        todoListDto.setId(todoList.getId());
        todoListDto.setIndex(todoList.getId().intValue());
        todoListDto.setTitle(todoList.getTitle());
        todoListDto.setUserId(todoList.getUser().getId());
        todoListDto.setWorkSpaceId(todoList.getWorkSpace().getId());
        todoListDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return todoListDto;
    }

    @Test
    public void TodoCardService_UploadTodoCardPhoto_ThrowBadRequestException() {
        TodoCard todoCardToUpdate = todoList.getTodoCards().get(0);
        byte[] fileContent = new byte[1024 * 1024 + 1024];
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "photo.jpg", "image/jpeg", fileContent);
        UploadTodoCardPhotoRequest request = new UploadTodoCardPhotoRequest(mockFile);

        Assertions.assertThatThrownBy(() -> {
            todoCardService.uploadTodoCardPhoto(todoCardToUpdate, request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("File size exceeded. Could not upload.");

    }

    @Test
    public void TodoCardService_UploadTodoCardPhoto_ReturnTodoCardDto() {
        TodoCard todoCardToUpdate = todoList.getTodoCards().get(0);
        byte[] fileContent = new byte[1024 * 256];
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "photo.jpg", "image/jpeg", fileContent);
        UploadTodoCardPhotoRequest request = new UploadTodoCardPhotoRequest(mockFile);

        doNothing().when(amazonService).deleteBucketObject("arrow-date",
                todoCardToUpdate.getUploadPhotoFileName());

        HashMap<String, String> result = new HashMap<String, String>();
        result.put("filename", mockFile.getOriginalFilename());
        result.put("objectUrl", "https://www.someurl.com/photo.jpeg");

        when(amazonService.putS3Object("arrow-date", mockFile.getOriginalFilename(), mockFile))
                .thenReturn(result);

        when(todoCardRepository.save(any(TodoCard.class))).thenReturn(todoCardToUpdate);

        TodoCardDto returnedTodoCardDto =
                todoCardService.uploadTodoCardPhoto(todoCardToUpdate, request);

        Assertions.assertThat(returnedTodoCardDto).isNotNull();

        verify(todoCardRepository, times(1)).save(any(TodoCard.class));

    }

}

