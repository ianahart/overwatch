package com.hart.overwatch.dropdownoption;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.customfield.CustomField;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.hamcrest.CoreMatchers;


@ActiveProfiles("test")
@WebMvcTest(controllers = DropDownOptionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DropDownOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DropDownOptionService dropDownOptionService;


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

    private List<CustomField> customFields;


    @BeforeEach
    public void setUp() {
        user = generateUser();

        workSpace = generatedWorkSpace(user);

        TodoList todoList = generateTodoList(user, workSpace);

        todoCard = generateTodoCard(user, todoList);

        customFields = generateCustomFields(user, todoCard);
        todoCard.setCustomFields(customFields);


    }

    private List<CustomField> generateCustomFields(User user, TodoCard todoCard) {
        List<CustomField> customFields = new ArrayList<>();
        String[] fieldTypes = {"NUMBER", "DROPDOWN", "TEXT", "CHECKBOX"};
        for (int i = 0; i < fieldTypes.length; i++) {
            CustomField customField = new CustomField();
            customField.setId(Long.valueOf(i + 1));
            customField.setIsActive(i % 2 == 0);
            customField.setFieldType(fieldTypes[i]);
            customField.setFieldName(String.format("fieldname-%d", i + 1));
            customField.setSelectedValue(i % 2 == 0 ? "value" : "");
            customField.setUser(user);
            customField.setTodoCard(todoCard);
            List<DropDownOption> dropDownOptions = new ArrayList<>();
            customField.setDropDownOptions(dropDownOptions);
            customFields.add(customField);
        }
        return customFields;
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
    public void DropDownOptionController_DeleteDropDownOption_ReturnDeleteDropDownOptionResponse()
            throws Exception {
        DropDownOption dropDownOption = new DropDownOption("option 1", customFields.get(1));
        dropDownOption.setId(1L);

        doNothing().when(dropDownOptionService).deleteDropDownOption(dropDownOption.getId());

        ResultActions response = mockMvc.perform(
                delete(String.format("/api/v1/dropdown-options/%d", dropDownOption.getId())));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));

    }
}


