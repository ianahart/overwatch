package com.hart.overwatch.todolist;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.todolist.request.CreateTodoListRequest;
import com.hart.overwatch.todolist.request.ReorderTodoListRequest;
import com.hart.overwatch.todolist.request.UpdateTodoListRequest;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.WorkSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = TodoListController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TodoListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoListService todoListService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private WorkSpace workSpace;


    private TodoList todoList;

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

    @Test
    public void TodoListController_ReorderTodoLists_Return_GetTodoListsResponse() throws Exception {
        List<TodoListDto> todoListDtos = createTodoListDtos(todoList);
        ReorderTodoListRequest request = new ReorderTodoListRequest(todoListDtos);

        when(todoListService.reorderTodoLists(anyLong(), anyList())).thenReturn(todoListDtos);

        ResultActions response = mockMvc.perform(
                post(String.format("/api/v1/workspaces/%d/todo-lists/reorder", workSpace.getId()))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(todoListDtos.get(0).getId().intValue())));
    }

    @Test
    public void TodoListController_CreateTodoList_ReturnCreateTodoListResponse() throws Exception {
        CreateTodoListRequest request =
                new CreateTodoListRequest("new todo list title", user.getId(), 1);
        TodoListDto todoListDto = createTodoListDtos(todoList).get(0);
        when(todoListService.createTodoList(any(CreateTodoListRequest.class), anyLong()))
                .thenReturn(todoListDto);

        ResultActions response = mockMvc
                .perform(post(String.format("/api/v1/workspaces/%d/todo-lists", workSpace.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void TodoListController_GetTodoLists_ReturnGetTodoListsResponse() throws Exception {
        List<TodoListDto> todoListDtos = createTodoListDtos(todoList);

        when(todoListService.getTodoListsByWorkSpace(workSpace.getId())).thenReturn(todoListDtos);

        ResultActions response = mockMvc
                .perform(get(String.format("/api/v1/workspaces/%d/todo-lists", workSpace.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(todoListDtos.get(0).getId().intValue())));

    }

    @Test
    public void TodoListController_UpdateTodoList_ReturnUpdateTodoListResponse() throws Exception {
        UpdateTodoListRequest request =
                new UpdateTodoListRequest(0, "updated title", workSpace.getId());
        TodoListDto todoListDto = createTodoListDtos(todoList).get(0);
        todoListDto.setTitle("updated title");

        when(todoListService.updateTodoList(anyLong(), any(UpdateTodoListRequest.class)))
                .thenReturn(todoListDto);

        ResultActions response =
                mockMvc.perform(patch(String.format("/api/v1/todo-lists/%d", todoList.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(request.getTitle())));
    }


    @Test
    public void TodoListController_DeleteTodoList_ReturnDeleteTodoListResponse() throws Exception {

        doNothing().when(todoListService).deleteTodoList(todoList.getId());

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/todo-lists/%d", todoList.getId())));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }
}

