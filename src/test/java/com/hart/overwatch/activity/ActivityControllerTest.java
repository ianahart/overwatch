package com.hart.overwatch.activity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.activity.request.CreateActivityRequest;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.data.domain.PageImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Collections;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = ActivityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

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

    private TodoCard todoCard;

    private Activity activity;


    @BeforeEach
    public void init() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        user.setId(1L);

        workSpace = new WorkSpace("work space title", "#0000FF", user);
        workSpace.setId(1L);
        todoList = new TodoList(user, workSpace, "todo list title", 0);
        todoList.setId(1L);
        todoCard = new TodoCard("todo card title", 0, user, todoList);
        todoCard.setId(1L);

        activity = new Activity("some activity text", user, todoCard);

        activity.setId(1L);
    }

    @Test
    public void ActivityController_CreateActivity_ReturnCreateActivityResponse() throws Exception {
        CreateActivityRequest request =
                new CreateActivityRequest(todoCard.getId(), user.getId(), "some activity text");

        ActivityDto activityDto = new ActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setText("some activity text");
        activityDto.setUserId(user.getId());
        activityDto.setAvatarUrl(user.getProfile().getAvatarUrl());
        activityDto.setTodoCardId(todoCard.getId());
        activityDto.setCreatedAt(LocalDateTime.now());

        when(activityService.handleCreateActivity(any(CreateActivityRequest.class)))
                .thenReturn(activityDto);

        ResultActions response =
                mockMvc.perform(post("/api/v1/activities").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id",
                        Matchers.equalToObject(activityDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.text",
                        CoreMatchers.is("some activity text")));
    }

    @Test
    public void ActivityController_GetWorkActivities_ReturnFetchActivityResponse()
            throws Exception {
        Long userId = user.getId();
        int page = 0;
        int pageSize = 3;
        String direction = "next";
        Pageable pageable = Pageable.ofSize(pageSize);
        ActivityDto activityDto = new ActivityDto();
        activityDto.setId(activity.getId());
        Page<ActivityDto> pageResult =
                new PageImpl<>(Collections.singletonList(activityDto), pageable, 1);
        PaginationDto<ActivityDto> paginationDto =
                new PaginationDto<>(pageResult.getContent(), pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());


        when(activityService.getActivities(todoCard.getId(), page, pageSize, direction))
                .thenReturn(paginationDto);

        ResultActions response = mockMvc
                .perform(get(String.format("/api/v1/todo-cards/%d/activities", todoCard.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", String.valueOf(userId)).param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)).param("direction", direction));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.items[0].id",
                        Matchers.equalToObject(activity.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page",
                        CoreMatchers.is(pageResult.getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalPages",
                        CoreMatchers.is(pageResult.getTotalPages())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.direction",
                        CoreMatchers.is(direction)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements",
                        CoreMatchers.is((int) pageResult.getTotalElements())));
    }

    @Test
    public void ActivityController_DeleteActivity_ReturnDeleteActivityResponse() throws Exception {

        doNothing().when(activityService).deleteActivity(activity.getId());

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/activities/%d", activity.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }


}

