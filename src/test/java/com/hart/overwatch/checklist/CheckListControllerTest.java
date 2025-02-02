package com.hart.overwatch.checklist;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.checklist.dto.CheckListDto;
import com.hart.overwatch.checklist.request.CreateCheckListRequest;
import com.hart.overwatch.checklistitem.CheckListItem;
import com.hart.overwatch.config.JwtService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = CheckListController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CheckListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CheckListService checkListService;

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

    private TodoCard todoCard;

    private List<CheckList> checkLists;

    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        checkLists = generateCheckLists(user, todoCard);
    }

    private List<CheckList> generateCheckLists(User user, TodoCard todoCard) {
        LocalDateTime timestamp = LocalDateTime.now();
        int toGenerate = 2;
        List<CheckList> checkLists = new ArrayList<>();

        for (int i = 1; i <= toGenerate; i++) {
            CheckList checkList = new CheckList();
            checkList.setId(Long.valueOf(i));
            checkList.setUser(user);
            checkList.setTodoCard(todoCard);
            checkList.setIsCompleted(false);
            checkList.setTitle(String.format("checklist-%d", i));
            checkList.setCreatedAt(timestamp);
            checkList.setUpdatedAt(timestamp);
            List<CheckListItem> checkListItems = new ArrayList<>();
            CheckListItem checkListItem = new CheckListItem();
            checkListItem.setUser(user);
            checkListItem.setCheckList(checkList);
            checkListItem.setTitle("checklistitem title");
            checkListItems.add(checkListItem);
            checkList.setCheckListItems(checkListItems);

            checkLists.add(checkList);
        }
        return checkLists;
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

    @Test
    public void CheckListController_CreateCheckList_ReturnCheckListResponse() throws Exception {
        CreateCheckListRequest request = new CreateCheckListRequest();
        request.setTitle("checklist-3");
        request.setUserId(user.getId());
        request.settodoCardId(todoCard.getId());

        doNothing().when(checkListService).createCheckList(request);

        ResultActions response =
                mockMvc.perform(post("/api/v1/checklists").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CheckListController_GetCheckLists_ReturnGetCheckListsResponse() throws Exception {
        LocalDateTime timestamp = LocalDateTime.now();
        Long todoCardId = todoCard.getId();
        CheckListDto checkListDto = new CheckListDto();
        CheckList checkList = checkLists.getFirst();
        checkListDto.setId(checkList.getId());
        checkListDto.setTitle(checkList.getTitle());
        checkListDto.setUserId(user.getId());
        checkListDto.setTodoCardId(todoCardId);
        checkListDto.setIsCompleted(false);
        checkListDto.setCreatedAt(timestamp);
        when(checkListService.getCheckLists(todoCardId)).thenReturn(List.of(checkListDto));

        ResultActions response =
                mockMvc.perform(get(String.format("/api/v1/todo-cards/%d/checklists", todoCardId)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CheckListController_DeleteCheckList_ReturnDeleteCheckListResponse()
            throws Exception {
        Long checkListId = checkLists.getFirst().getId();
        doNothing().when(checkListService).deleteCheckList(checkListId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/checklists/%d", checkListId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

}


