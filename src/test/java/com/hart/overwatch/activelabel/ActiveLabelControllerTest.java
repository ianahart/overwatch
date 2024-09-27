package com.hart.overwatch.activelabel;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import com.hart.overwatch.activelabel.ActiveLabel;
import com.hart.overwatch.activelabel.request.CreateActiveLabelRequest;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.label.Label;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = ActiveLabelController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ActiveLabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActiveLabelService activeLabelService;

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

    private TodoCard todoCard;

    private List<Label> labels;

    private ActiveLabel activeLabel;

    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        labels = generateLabels(user, workSpace);

        activeLabel = generateActiveLabel(labels.get(0), todoCard);
    }

    private TodoList generateTodoList(User user, WorkSpace workSpace) {
        TodoList todoList = new TodoList();
        todoList.setId(1L);
        todoList.setUser(user);
        todoList.setWorkSpace(workSpace);
        todoList.setTitle("title");
        todoList.setIndex(0);

        return todoList;
    }

    private ActiveLabel generateActiveLabel(Label label, TodoCard todoCard) {
        ActiveLabel activeLabel = new ActiveLabel();
        activeLabel.setId(1L);
        activeLabel.setLabel(label);
        activeLabel.setTodoCard(todoCard);

        return activeLabel;
    }

    private TodoCard generateTodoCard(User user, TodoList todoList) {
        TodoCard todoCard = new TodoCard();
        todoCard.setId(1L);
        todoCard.setUser(user);
        todoCard.setTitle("card title");
        todoCard.setIndex(0);
        todoCard.setTodoList(todoList);

        return todoCard;
    }

    private WorkSpace generatedWorkSpace(User user) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(1L);
        workSpace.setTitle("main");
        workSpace.setBackgroundColor("#000000");
        workSpace.setUser(user);

        return workSpace;
    }

    private User generateUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setFullName("John Doe");
        user.setEmail("john@mail.com");
        user.setRole(Role.USER);
        user.setLoggedIn(true);
        user.setProfile(new Profile());
        user.setPassword("Test12345%");
        user.setSetting(new Setting());

        return user;
    }

    private List<Label> generateLabels(User user, WorkSpace workSpace) {
        LocalDateTime timestamp = LocalDateTime.now();
        String[] titles = new String[] {"priority", "warning"};
        List<Label> labels = new ArrayList<>();
        int toGenerate = 2;
        for (int i = 1; i <= toGenerate; i++) {
            Label label = new Label();
            label.setId(Long.valueOf(i));
            label.setUser(user);
            label.setWorkSpace(workSpace);
            label.setColor("#000000");
            label.setTitle(titles[i - 1]);
            label.setIsChecked(false);
            label.setCreatedAt(timestamp);
            label.setUpdatedAt(timestamp);
            labels.add(label);
        }
        return labels;
    }


    @Test
    public void ActiveLabelController_CreateActiveLabel_ReturnCreateActiveLabelResponse()
            throws Exception {
        Long labelId = labels.get(0).getId();
        Long todoCardId = todoCard.getId();

        CreateActiveLabelRequest request = new CreateActiveLabelRequest();
        request.setLabelId(labelId);
        request.setTodoCardId(todoCardId);

        doNothing().when(activeLabelService).createActiveLabel(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/active-labels").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void ActiveLabelController_DeleteActiveLabel_ReturnDeleteActiveLabelResponse()
            throws Exception {
        Long todoCardId = todoCard.getId();
        Long labelId = labels.get(0).getId();

        doNothing().when(activeLabelService).deleteActiveLabel(todoCardId, labelId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/active-labels/%d", todoCardId)).param("labelId", "1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }
}


