package com.hart.overwatch.customfield;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.customfield.dto.CustomFieldDto;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.customfield.request.UpdateCustomFieldRequest;
import com.hart.overwatch.customfieldmanagement.CustomFieldManagementService;
import com.hart.overwatch.dropdownoption.DropDownOption;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionPayloadDto;
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
@WebMvcTest(controllers = CustomFieldController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CustomFieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomFieldService customFieldService;

    @MockBean
    private CustomFieldManagementService customFieldManagementService;

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

    private List<DropDownOptionPayloadDto> generatedDropDownOptionPayloadDtos() {
        List<DropDownOptionPayloadDto> dropDownOptionPayloadDtos = new ArrayList<>();
        dropDownOptionPayloadDtos.add(new DropDownOptionPayloadDto("1", "option 1"));
        dropDownOptionPayloadDtos.add(new DropDownOptionPayloadDto("2", "option 2"));
        dropDownOptionPayloadDtos.add(new DropDownOptionPayloadDto("3", "option 3"));

        return dropDownOptionPayloadDtos;
    }

    private CreateCustomFieldRequest createCustomFieldRequest(String fieldName, Long userId,
            Long todoCardId) {
        CreateCustomFieldRequest request = new CreateCustomFieldRequest();
        request.setUserId(userId);
        request.setTodoCardId(todoCardId);
        request.setFieldName(fieldName);
        request.setFieldType("DROPDOWN");
        request.setSelectedValue("");
        request.setDropDownOptions(generatedDropDownOptionPayloadDtos());
        request.setDropDownOptions(null);

        return request;

    }

    private List<CustomFieldDto> generateCustomFieldDtos(List<CustomField> customFields) {
        return customFields.stream().map(customField -> {
            CustomFieldDto customFieldDto = new CustomFieldDto();
            customFieldDto.setId(customField.getId());
            customFieldDto.setUserId(customField.getUser().getId());
            customFieldDto.setTodoCardId(customField.getTodoCard().getId());
            customFieldDto.setIsActive(customField.getIsActive());
            customFieldDto.setFieldName(customField.getFieldName());
            customFieldDto.setFieldType(customField.getFieldType());
            return customFieldDto;
        }).filter(customField -> customField.getIsActive()).toList();
    }

    @Test
    public void CustomFieldController_CreateCustomField_ReturnCreateCustomFieldResponse()
            throws Exception {
        CreateCustomFieldRequest request =
                createCustomFieldRequest("fieldname-5", user.getId(), todoCard.getId());
        doNothing().when(customFieldManagementService).handleCreateCustomField(request);

        ResultActions response = mockMvc
                .perform(post("/api/v1/custom-fields").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CustomFieldController_GetCustomFields_ReturnGetCustomFieldsResponse()
            throws Exception {
        List<CustomFieldDto> customFieldDtos = generateCustomFieldDtos(customFields);
        when(customFieldService.getCustomFields(todoCard.getId(), "true"))
                .thenReturn(customFieldDtos);

        ResultActions response = mockMvc
                .perform(get(String.format("/api/v1/todo-cards/%d/custom-fields", todoCard.getId()))
                        .param("isActive", "true"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
                        CoreMatchers.is(customFieldDtos.getFirst().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id",
                        CoreMatchers.is(customFieldDtos.getLast().getId().intValue())));
    }

    @Test
    public void CustomFieldController_DeleteCustomField_ReturnDeleteCustomFieldResponse()

            throws Exception {
        Long customFieldId = customFields.getFirst().getId();
        doNothing().when(customFieldService).deleteCustomField(customFieldId);

        ResultActions response =
                mockMvc.perform(delete(String.format("/api/v1/custom-fields/%d", customFieldId)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void CustomFieldController_UpdateCustomField_ReturnUpdateCustomFieldResponse()
            throws Exception {
        CustomField customField = customFields.getFirst();
        UpdateCustomFieldRequest request = new UpdateCustomFieldRequest(true);
        doNothing().when(customFieldService).updateCustomField(customField.getId(), request);

        ResultActions response = mockMvc
                .perform(patch(String.format("/api/v1/custom-fields/%d", customField.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }
}


