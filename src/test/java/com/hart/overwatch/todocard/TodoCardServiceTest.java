package com.hart.overwatch.todocard;

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
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.amazon.AmazonService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
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

    @Test
    public void TodoCardService_GetTodoCardById_ThrowNotFoundException() {
        Long nonExistentTodoCardId = 999L;
        when(todoCardRepository.findById(nonExistentTodoCardId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            todoCardService.getTodoCardById(nonExistentTodoCardId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("A todo card with the id %d was not found", nonExistentTodoCardId));
    }

    @Test
    public void TodoCardService_GetTodoCardById_ReturnTodoCard() {
        TodoCard todoCard = todoList.getTodoCards().get(0);
        when(todoCardRepository.findById(todoCard.getId())).thenReturn(Optional.of(todoCard));

        TodoCard returnedTodoCard = todoCardService.getTodoCardById(todoCard.getId());

        Assertions.assertThat(returnedTodoCard).isNotNull();
        Assertions.assertThat(returnedTodoCard.getId()).isEqualTo(todoCard.getId());
    }

    @Test
    public void TodoCardService_CreateTodoCard_ThrowBadRequestExceptionMax() {
        Long todoListId = todoList.getId();
        Long userId = user.getId();
        long MAX_TODO_CARDS_QUANTITY = 10L;
        CreateTodoCardRequest request = new CreateTodoCardRequest("card title", 0, userId);

        when(todoCardRepository.countTodoCardsInTodoList(todoListId, userId))
                .thenReturn(MAX_TODO_CARDS_QUANTITY + 1);

        Assertions.assertThatThrownBy(() -> {
            todoCardService.createTodoCard(todoListId, request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage(String.format(
                        "You have added the maximum amount of cards (%d) for this list",
                        MAX_TODO_CARDS_QUANTITY));
    }

    @Test
    public void TodoCardService_CreateTodoCard_ThrowBadRequestExceptionExists() {
        Long todoListId = todoList.getId();
        Long userId = user.getId();
        long MAX_TODO_CARDS_QUANTITY = 10L;
        CreateTodoCardRequest request = new CreateTodoCardRequest("title-1", 0, userId);

        when(todoCardRepository.countTodoCardsInTodoList(todoListId, userId))
                .thenReturn(MAX_TODO_CARDS_QUANTITY - 1);
        when(todoListService.getTodoListById(todoListId)).thenReturn(todoList);

        Assertions.assertThatThrownBy(() -> {
            todoCardService.createTodoCard(todoListId, request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("You have already added a card with that title to this list");
    }

    @Test
    public void TodoCardService_CreateTodoCard_ReturnTodoCardDto() {
        Long todoListId = todoList.getId();
        Long userId = user.getId();
        long MAX_TODO_CARDS_QUANTITY = 10L;
        CreateTodoCardRequest request = new CreateTodoCardRequest("title-4", 2, userId);

        when(todoCardRepository.countTodoCardsInTodoList(todoListId, userId))
                .thenReturn(MAX_TODO_CARDS_QUANTITY - 1);
        when(todoListService.getTodoListById(todoListId)).thenReturn(todoList);
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(todoListService.getTodoListById(todoListId)).thenReturn(todoList);

        TodoCard newTodoCard = new TodoCard(Jsoup.clean(request.getTitle(), Safelist.none()),
                request.getIndex(), user, todoList);

        when(todoCardRepository.save(any(TodoCard.class))).thenReturn(newTodoCard);

        TodoCardDto todoCardDto = todoCardService.createTodoCard(todoListId, request);

        Assertions.assertThat(todoCardDto).isNotNull();
        Assertions.assertThat(todoCardDto.getIndex()).isEqualTo(newTodoCard.getIndex());
        Assertions.assertThat(todoCardDto.getTitle()).isEqualTo(newTodoCard.getTitle());
    }

    @Test
    public void TodoCardService_DeleteTodoCard_ThrowForbiddenException() {
        TodoCard todoCard = todoList.getTodoCards().get(0);
        User forbiddenUser = new User();
        forbiddenUser.setId(999L);

        when(todoCardRepository.findById(todoCard.getId())).thenReturn(Optional.of(todoCard));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            todoCardService.deleteTodoCard(todoCard.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("You cannot delete another user's card");
    }

    @Test
    public void TodoCardService_DeleteTodoCard_ReturnNothing() {
        TodoCard todoCard = todoList.getTodoCards().get(0);
        when(todoCardRepository.findById(todoCard.getId())).thenReturn(Optional.of(todoCard));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoList);
        doNothing().when(todoCardRepository).delete(todoCard);

        todoCardService.deleteTodoCard(todoCard.getId());

        verify(todoListRepository, times(1)).save(any(TodoList.class));
        verify(todoCardRepository, times(1)).delete(todoCard);
    }

    @Test
    public void TodoCardService_ReorderTodoCards_ReturnNothing() {
        when(todoListService.getTodoListById(todoList.getId())).thenReturn(todoList);
        ReorderTodoCardRequest request = new ReorderTodoCardRequest(todoList.getId(), 2, 1);
        when(todoCardRepository.saveAll(anyList())).thenReturn(todoList.getTodoCards());

        todoCardService.reorderTodoCards(request, 1L);

        verify(todoCardRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void TodoCardService_moveTodoCards_ReturnNothing() {
        Long todoCardId = 1L;
        Long destinationListId = 2L;
        Long sourceListId = 3L;

        MoveTodoCardRequest request = new MoveTodoCardRequest();
        request.setDestinationListId(destinationListId);
        request.setNewIndex(1);

        TodoList sourceList = new TodoList();
        sourceList.setId(sourceListId);

        TodoList destinationList = new TodoList();
        destinationList.setId(destinationListId);

        TodoCard todoCardToMove = new TodoCard("Card to move", 0, user, sourceList);
        todoCardToMove.setId(todoCardId);

        TodoCard anotherSourceCard = new TodoCard("Another source card", 1, user, sourceList);

        sourceList.setTodoCards(Arrays.asList(todoCardToMove, anotherSourceCard));

        TodoCard existingDestCard =
                new TodoCard("Existing card in destination", 0, user, destinationList);
        destinationList.setTodoCards(Collections.singletonList(existingDestCard));

        when(todoCardRepository.findById(todoCardId)).thenReturn(Optional.of(todoCardToMove));

        todoCardService.moveTodoCards(todoCardId, request, destinationList, sourceList);

        Assertions.assertThat(todoCardToMove.getTodoList().getId()).isEqualTo(destinationListId);

        Assertions.assertThat(sourceList.getTodoCards()).doesNotContain(todoCardToMove);
        Assertions.assertThat(sourceList.getTodoCards()).hasSize(1);

        Assertions.assertThat(destinationList.getTodoCards()).contains(todoCardToMove);
        Assertions.assertThat(destinationList.getTodoCards()).hasSize(2);


        Assertions.assertThat(existingDestCard.getIndex()).isEqualTo(0);

        Assertions.assertThat(todoCardToMove.getIndex()).isEqualTo(1);

        verify(todoListRepository).save(sourceList);
        verify(todoListRepository).save(destinationList);
    }
}


