package com.hart.overwatch.checklistitem;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.ctc.wstx.shaded.msv_core.datatype.xsd.AnyURIType;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.checklist.dto.CheckListDto;
import com.hart.overwatch.checklist.request.CreateCheckListRequest;
import com.hart.overwatch.checklistitem.CheckListItem;
import com.hart.overwatch.checklistitem.dto.CheckListItemDto;
import com.hart.overwatch.checklistitem.request.CreateCheckListItemRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.hamcrest.CoreMatchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = CheckListItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CheckListItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckListItemService checkListItemService;

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

    private CheckList checkList;

    private List<CheckListItem> checkListItems;

    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        checkList = generateCheckList(user, todoCard);

        checkListItems = generateCheckListItems(user, checkList);
    }

    private List<CheckListItem> generateCheckListItems(User user, CheckList checkList) {
        List<CheckListItem> checkListItems = new ArrayList<>();
        int toGenerate = 2;

        for (int i = 1; i <= toGenerate; i++) {
            CheckListItem checkListItem = new CheckListItem();
            checkListItem.setId(Long.valueOf(i));
            checkListItem.setUser(user);
            checkListItem.setTitle(String.format("checklistitem-%d", i));
            checkListItem.setCheckList(checkList);
            checkListItem.setIsCompleted(false);
            checkListItems.add(checkListItem);
        }
        return checkListItems;
    }

    private CheckList generateCheckList(User user, TodoCard todoCard) {
        LocalDateTime timestamp = LocalDateTime.now();
        CheckList checkList = new CheckList();
        checkList.setId(1L);
        checkList.setUser(user);
        checkList.setTodoCard(todoCard);
        checkList.setIsCompleted(false);
        checkList.setTitle(String.format("checklist-1"));
        checkList.setCreatedAt(timestamp);
        checkList.setUpdatedAt(timestamp);
        return checkList;
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
    public void CheckListItemController_CreateCheckListItem_ReturnCreateCheckListItemResponse()
            throws Exception {
        CreateCheckListItemRequest request = new CreateCheckListItemRequest();
        request.setTitle("checklistitem-3");
        request.setUserId(user.getId());
        request.setCheckListId(checkList.getId());

        CheckListItemDto checkListItemDto = new CheckListItemDto();
        checkListItemDto.setTitle(request.getTitle());
        checkListItemDto.setUserId(request.getUserId());
        checkListItemDto.setCheckListId(request.getCheckListId());

        when(checkListItemService.createCheckListItem(any(CreateCheckListItemRequest.class)))
                .thenReturn(checkListItemDto);

        ResultActions response = mockMvc
                .perform(post("/api/v1/checklist-items").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title",
                        CoreMatchers.is(checkListItemDto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId",
                        CoreMatchers.is(checkListItemDto.getUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.checkListId",
                        CoreMatchers.is(checkListItemDto.getCheckListId().intValue())));
    }
}


