package com.hart.overwatch.todocard;


import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todocard.request.CreateTodoCardRequest;
import com.hart.overwatch.todocard.request.ReorderTodoCardRequest;
import com.hart.overwatch.todocard.request.UpdateTodoCardRequest;
import com.hart.overwatch.todocardmanagement.TodoCardManagementService;
import com.hart.overwatch.todolist.TodoList;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = TodoCardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TodoCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoCardManagementService todoCardManagementService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
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
    public void TodoCardController_CreateTodoCard_ReturnCreateTodoCardResponse() throws Exception {
        Long todoListId = todoList.getId();
        CreateTodoCardRequest request = new CreateTodoCardRequest("title-4", 2, user.getId());
        TodoCardDto todoCardDto = new TodoCardDto();
        todoCardDto.setId(3L);
        todoCardDto.setIndex(request.getIndex());
        todoCardDto.setTitle(request.getTitle());
        when(todoCardManagementService.handleCreateTodoCard(anyLong(),
                any(CreateTodoCardRequest.class))).thenReturn(todoCardDto);

        ResultActions response =
                mockMvc.perform(post(String.format("/api/v1/todo-lists/%d/todo-cards", todoListId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        Matchers.equalToObject(todoCardDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.index",
                        CoreMatchers.is(todoCardDto.getIndex())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(todoCardDto.getTitle())));
    }

    @Test
    public void TodoCardController_UpdateTodoCard_ReturnUpdateTodoCardResponse() throws Exception {
        Long todoCardId = todoList.getTodoCards().get(0).getId();
        LocalDateTime timestamp = LocalDateTime.now();
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

        when(todoCardManagementService.handleUpdateTodoCard(anyLong(),
                any(UpdateTodoCardRequest.class))).thenReturn(todoCardDto);

        ResultActions response =
                mockMvc.perform(put(String.format("/api/v1/todo-cards/%d", todoCardId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        CoreMatchers.is(todoCardDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.index",
                        CoreMatchers.is(todoCardDto.getIndex())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(todoCardDto.getTitle())));
    }

    @Test
    public void TodoCardController_DeleteTodoCard_ReturnDeleteTodoCardResponse() throws Exception {
        Long todoCardId = todoList.getTodoCards().get(0).getId();

        doNothing().when(todoCardManagementService).handleDeleteTodoCard(todoCardId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/todo-cards/%d", todoCardId)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void TodoCardController_ReorderTodoCard_ReturnReorderTodoCardResponse()
            throws Exception {
        Long todoCardId = todoList.getTodoCards().get(0).getId();
        ReorderTodoCardRequest request = new ReorderTodoCardRequest(todoList.getId(), 2, 1);
        doNothing().when(todoCardManagementService).handleReorderTodoCards(request, todoCardId);

        ResultActions response =
                mockMvc.perform(patch(String.format("/api/v1/todo-cards/%d/reorder", todoCardId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }


}


