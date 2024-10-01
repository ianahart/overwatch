package com.hart.overwatch.customfieldmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.activity.ActivityService;
import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.customfield.CustomField;
import com.hart.overwatch.customfield.CustomFieldService;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.dropdownoption.DropDownOption;
import com.hart.overwatch.dropdownoption.DropDownOptionService;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionPayloadDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CustomFieldManagementServiceTest {

    @InjectMocks
    private CustomFieldManagementService customFieldManagementService;

    @Mock
    CustomFieldService customFieldService;

    @Mock
    DropDownOptionService dropDownOptionService;

    @Mock
    ActivityService activityService;

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

    private List<DropDownOption> generateDropDownOptions(int times, CustomField customField) {
        List<DropDownOption> dropDownOptions = new ArrayList<>();
        for (int i = 1; i <= times; i++) {
            DropDownOption dropDownOption = new DropDownOption();
            dropDownOption.setOptionValue(String.format("option-%d", i));
            dropDownOption.setId(Long.valueOf(i));
            dropDownOption.setCustomField(customField);
            dropDownOptions.add(dropDownOption);
        }
        return dropDownOptions;
    }


    @Test
    public void CustomFieldManagementService_HandleCreateCustomField_ReturnNothing() {
        CreateCustomFieldRequest request = new CreateCustomFieldRequest();

        request.setUserId(user.getId());
        request.setFieldName("fieldname-4");
        request.setFieldType("DROPDOWN");
        request.setSelectedValue(null);
        request.setTodoCardId(todoCard.getId());
        request.setDropDownOptions(generatedDropDownOptionPayloadDtos());

        CustomField newCustomField = new CustomField();
        newCustomField.setId(4L);
        newCustomField.setUser(user);
        newCustomField.setTodoCard(todoCard);
        newCustomField.setIsActive(false);
        newCustomField.setFieldName(request.getFieldName());
        newCustomField.setFieldType(request.getFieldType());
        newCustomField.setSelectedValue(null);
        newCustomField.setDropDownOptions(generateDropDownOptions(3, newCustomField));

        when(customFieldService.createCustomField(request)).thenReturn(newCustomField);

        doNothing().when(dropDownOptionService).createDropDownOptions(request.getDropDownOptions(),
                newCustomField);

        String text = String.format("You added a %s custom field called %s",
                newCustomField.getFieldType(), newCustomField.getFieldName());
        when(activityService.createActivity(text, newCustomField.getTodoCard().getId(),
                newCustomField.getUser().getId())).thenReturn(new ActivityDto());

        customFieldManagementService.handleCreateCustomField(request);

        verify(dropDownOptionService, times(1)).createDropDownOptions(request.getDropDownOptions(),
                newCustomField);
        verify(activityService, times(1)).createActivity(text, newCustomField.getTodoCard().getId(),
                newCustomField.getUser().getId());
    }
}


