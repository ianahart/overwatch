package com.hart.overwatch.todolist;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardRepository;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.request.UpdateTodoListRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceService;
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

    private List<TodoCard> cards;


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

        TodoCard todoCard = new TodoCard();
        todoCard.setId(1L);
        cards = List.of(todoCard);

        todoList.setTodoCards(cards);
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

    @Test
    public void TodoListService_CreateTodoList_ReturnTodoListDto() {
        CreateTodoListRequest request = new CreateTodoListRequest();
        request.setTitle("todo list title");
        request.setUserId(user.getId());
        request.setIndex(1);

        when(todoListRepository.countTodoListsInWorkSpace(workSpace.getId(), user.getId()))
                .thenReturn(1L);
        when(todoListRepository.findTodoListByWorkSpaceAndUserAndTitle(workSpace.getId(),
                user.getId(), request.getTitle())).thenReturn(false);
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(workSpaceService.getWorkSpaceById(workSpace.getId())).thenReturn(workSpace);

        when(todoListRepository.save(any(TodoList.class))).thenAnswer(invocation -> {
            TodoList savedTodoList = invocation.getArgument(0);
            savedTodoList.setId(1L);
            return savedTodoList;
        });

        TodoListDto returnedTodoListDto =
                todoListService.createTodoList(request, workSpace.getId());

        Assertions.assertThat(returnedTodoListDto).isNotNull();
        Assertions.assertThat(returnedTodoListDto.getId()).isEqualTo(todoList.getId());
    }

    @Test
    public void TodoListService_GetTodoListsByWorkSpace_ThrowNotFoundException() {
        Long nonExistentWorkSpaceId = 999L;
        when(workSpaceService.workSpaceExists(nonExistentWorkSpaceId)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
            todoListService.getTodoListsByWorkSpace(nonExistentWorkSpaceId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(
                        "No workspace exists for id %d. Cannot fetch non existent todo lists",
                        nonExistentWorkSpaceId));
    }

    @Test
    public void TodoListService_GetTodoListsByWorkSpace_ReturnTodoListsWithCards() {
        Long workSpaceId = 1L;
        int MAX_TODO_LISTS = 10;
        Pageable pageable = PageRequest.of(0, MAX_TODO_LISTS);

        when(workSpaceService.workSpaceExists(workSpaceId)).thenReturn(true);

        TodoList todoList1 = new TodoList(user, workSpace, "Todo list 1", 0);
        TodoList todoList2 = new TodoList(user, workSpace, "Todo list 2", 1);

        todoList1.setId(1L);
        todoList2.setId(2L);

        TodoCardDto todoCardDto1 = createTodoCardDto(1L, todoList1);
        TodoCardDto todoCardDto2 = createTodoCardDto(2L, todoList2);

        List<TodoListDto> todoListDtos =
                List.of(createTodoListDto(todoList1), createTodoListDto(todoList2));
        Page<TodoListDto> todoListPage = new PageImpl<>(todoListDtos);

        when(todoListRepository.getTodoListsByWorkSpace(pageable, workSpaceId))
                .thenReturn(todoListPage);
        when(todoCardService.retrieveTodoCards(1L)).thenReturn(List.of(todoCardDto1));
        when(todoCardService.retrieveTodoCards(2L)).thenReturn(List.of(todoCardDto2));

        List<TodoListDto> returnedTodoLists = todoListService.getTodoListsByWorkSpace(workSpaceId);

        Assertions.assertThat(returnedTodoLists).isNotNull();
        Assertions.assertThat(returnedTodoLists).hasSize(2);
        Assertions.assertThat(returnedTodoLists.get(0).getCards().size()).isEqualTo(1);
        Assertions.assertThat(returnedTodoLists.get(1).getCards().size()).isEqualTo(1);
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
    public void TodoListService_ReorderTodoLists_ReturnListOfTodoListDto() {
        Long workSpaceId = 1L;

        TodoListDto todoListDto1 = new TodoListDto(1L, 1L, workSpaceId, "List 1", 0, null);
        TodoListDto todoListDto2 = new TodoListDto(2L, 1L, workSpaceId, "List 2", 1, null);
        TodoListDto todoListDto3 = new TodoListDto(3L, 1L, workSpaceId, "List 3", 2, null);

        List<TodoListDto> todoListDtos = Arrays.asList(todoListDto2, todoListDto3, todoListDto1);

        TodoList todoList1 = new TodoList();
        todoList1.setId(1L);
        todoList1.setIndex(0);

        TodoList todoList2 = new TodoList();
        todoList2.setId(2L);
        todoList2.setIndex(1);

        TodoList todoList3 = new TodoList();
        todoList3.setId(3L);
        todoList3.setIndex(2);

        when(todoListRepository.findById(1L)).thenReturn(Optional.of(todoList1));
        when(todoListRepository.findById(2L)).thenReturn(Optional.of(todoList2));
        when(todoListRepository.findById(3L)).thenReturn(Optional.of(todoList3));

        when(todoListRepository.saveAll(anyList()))
                .thenReturn(Arrays.asList(todoList1, todoList2, todoList3));

        when(workSpaceService.workSpaceExists(workSpaceId)).thenReturn(true);

        when(todoListRepository.getTodoListsByWorkSpace(any(PageRequest.class), eq(workSpaceId)))
                .thenReturn(new PageImpl<>(todoListDtos));

        when(todoCardService.retrieveTodoCards(anyLong())).thenReturn(Arrays.asList());

        List<TodoListDto> reorderedTodoLists =
                todoListService.reorderTodoLists(workSpaceId, todoListDtos);

        assertNotNull(reorderedTodoLists);
        assertEquals(3, reorderedTodoLists.size());

        assertEquals(0, reorderedTodoLists.get(0).getIndex());
        assertEquals(1, reorderedTodoLists.get(1).getIndex());
        assertEquals(2, reorderedTodoLists.get(2).getIndex());

        verify(todoListRepository, times(1)).saveAll(anyList());
        verify(todoListRepository, times(3)).findById(anyLong());
    }

    @Test
    public void TodoListService_UpdateTodoList_ThrowForbiddenException() {
        UpdateTodoListRequest request =
                new UpdateTodoListRequest(1, "updated title", workSpace.getId());
        User forbiddenUser = new User();
        forbiddenUser.setId(999L);

        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);
        when(todoListRepository.findTodoListByWorkSpaceAndUserAndTitle(workSpace.getId(),
                forbiddenUser.getId(), "updated title")).thenReturn(false);
        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));

        Assertions.assertThatThrownBy(() -> {
            todoListService.updateTodoList(todoList.getId(), request);
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot edit a todo list that is not yours");

    }

    @Test
    public void TodoListService_UpdateTodoList_ReturnTodoListDto() {
        UpdateTodoListRequest request =
                new UpdateTodoListRequest(1, "updated title", workSpace.getId());

        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        when(todoListRepository.findTodoListByWorkSpaceAndUserAndTitle(workSpace.getId(),
                user.getId(), request.getTitle())).thenReturn(false);
        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));

        when(todoListRepository.save(any(TodoList.class))).thenReturn(todoList);
        when(todoCardService.retrieveTodoCards(todoList.getId()))
                .thenReturn(List.of(new TodoCardDto()));
        TodoListDto todoListDto = todoListService.updateTodoList(todoList.getId(), request);

        Assertions.assertThat(todoListDto).isNotNull();
        Assertions.assertThat(todoListDto.getTitle()).isEqualTo(request.getTitle());
        Assertions.assertThat(todoListDto.getCards().size()).isEqualTo(1);

        verify(todoListRepository, times(1)).save(any(TodoList.class));
    }

    @Test
    public void TodoListService_DeleteTodoList_ThrowForbiddenException() {
        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));
        User forbiddenUser = new User();
        forbiddenUser.setId(999L);
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            todoListService.deleteTodoList(todoList.getId());
        }).isInstanceOf(ForbiddenException.class).hasMessage("Cannot delete a todo list that is not yours");
    }


    private TodoCard createTodoCard(Long id) {
        TodoCard todoCard = new TodoCard();
        todoCard.setId(id);

        return todoCard;
    }

    @Test
    public void TodoListService_DeleteTodoList_ReturnNothing() {
        todoList.setTodoCards(List.of(createTodoCard(1L), createTodoCard(2L)));

        when(todoListRepository.findById(todoList.getId())).thenReturn(Optional.of(todoList));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);

        doNothing().when(todoListRepository).delete(todoList);

        todoListService.deleteTodoList(todoList.getId());

        verify(todoListRepository, times(1)).delete(todoList);
        verify(todoCardRepository, times(1)).delete(todoList.getTodoCards().get(0));
        verify(todoCardRepository, times(1)).delete(todoList.getTodoCards().get(1));
    }

}
