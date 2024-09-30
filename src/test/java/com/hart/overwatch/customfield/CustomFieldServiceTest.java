package com.hart.overwatch.customfield;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.customfield.dto.CustomFieldDto;
import com.hart.overwatch.customfield.request.CreateCustomFieldRequest;
import com.hart.overwatch.dropdownoption.DropDownOption;
import com.hart.overwatch.dropdownoption.dto.DropDownOptionPayloadDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardService;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.workspace.WorkSpace;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CustomFieldServiceTest {

    @InjectMocks
    private CustomFieldService customFieldService;

    @Mock
    CustomFieldRepository customFieldRepository;

    @Mock
    TodoCardService todoCardService;

    @Mock
    UserService userService;

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

    @Test
    public void CustomFieldService_GetCustomFieldById_ThrowNotFoundException() {
        Long nonExistentCustomFieldId = 999L;

        when(customFieldRepository.findById(nonExistentCustomFieldId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            customFieldService.getCustomFieldById(nonExistentCustomFieldId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("Could not find custom field with id %d", nonExistentCustomFieldId));
    }

    @Test
    public void CustomFieldService_GetCustomFieldById_ReturnCustomField() {
        CustomField customField = customFields.getFirst();

        when(customFieldRepository.findById(customField.getId()))
                .thenReturn(Optional.of(customField));

        CustomField returnedCustomField =
                customFieldService.getCustomFieldById(customField.getId());

        Assertions.assertThat(returnedCustomField).isNotNull();
        Assertions.assertThat(returnedCustomField.getId()).isEqualTo(customField.getId());
    }

    @Test
    public void CustomFieldService_GetCustomFields_ThrowBadRequestExceptionMissingParams() {
        Long todoCardId = null;

        Assertions.assertThatThrownBy(() -> {
            customFieldService.getCustomFields(todoCardId, "true");
        }).isInstanceOf(BadRequestException.class).hasMessage("Missing todo card id parameter");
    }

    @Test
    public void CustomFieldService_GetCustomFields_ReturnListOfCustomFieldDtoActive() {
        Long todoCardId = todoCard.getId();
        String isActiveParam = "true";

        List<CustomField> activeCustomFields =
                customFields.stream().filter(v -> v.getIsActive()).toList();

        when(customFieldRepository.findByTodoCardIdAndIsActive(todoCardId, true))
                .thenReturn(activeCustomFields);

        List<CustomFieldDto> customFieldDtos =
                customFieldService.getCustomFields(todoCardId, isActiveParam);

        Assertions.assertThat(customFieldDtos).isNotNull();
        Assertions.assertThat(customFieldDtos.size()).isEqualTo(2);
    }

    @Test
    public void CustomFieldService_GetCustomFields_ReturnListOfAllCustomFieldDto() {
        Long todoCardId = todoCard.getId();
        String isActiveParam = "false";

        when(customFieldRepository.findByTodoCardId(todoCardId)).thenReturn(customFields);

        List<CustomFieldDto> customFieldDtos =
                customFieldService.getCustomFields(todoCardId, isActiveParam);

        Assertions.assertThat(customFieldDtos).isNotNull();
        Assertions.assertThat(customFieldDtos.size()).isEqualTo(todoCard.getCustomFields().size());
    }

    @Test
    public void CustomFieldService_CreateCustomField_ThrowBadRequestExceptionMissingParams() {
        CreateCustomFieldRequest request =
                createCustomFieldRequest("fieldname-5", null, todoCard.getId());

        Assertions.assertThatThrownBy(() -> {
            customFieldService.createCustomField(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Missing either userId or todoCardId in payload");
    }

    @Test
    public void CustomFieldService_CreateCustomField_ThrowBadRequestExceptionMaxFields() {
        int MAX_CUSTOM_FIELDS = 10;
        CreateCustomFieldRequest request =
                createCustomFieldRequest("fieldname-5", user.getId(), todoCard.getId());

        when(customFieldRepository.countCustomFieldsPerTodoCard(user.getId(), todoCard.getId()))
                .thenReturn(Long.valueOf(MAX_CUSTOM_FIELDS + 1));

        Assertions.assertThatThrownBy(() -> {
            customFieldService.createCustomField(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage(String.format(
                        "You have already added the maximum amount of custom fields (%d)",
                        MAX_CUSTOM_FIELDS));
    }

    @Test
    public void CustomFieldService_CreateCustomField_ThrowBadRequestExceptionAlreadyExists() {
        CreateCustomFieldRequest request =
                createCustomFieldRequest("fieldname-2", user.getId(), todoCard.getId());

        when(customFieldRepository.countCustomFieldsPerTodoCard(user.getId(), todoCard.getId()))
                .thenReturn(0L);
        when(customFieldRepository.alreadyExistsByFieldNameNotType(todoCard.getId(),
                request.getFieldName(), request.getFieldType())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            customFieldService.createCustomField(request);
        }).isInstanceOf(BadRequestException.class);
    }

    @Test
    public void CustomFieldService_CreateCustomField_CreateCustomField_ReturnCustomField() {
        CreateCustomFieldRequest request =
                createCustomFieldRequest("fieldname-6", user.getId(), todoCard.getId());

        when(customFieldRepository.countCustomFieldsPerTodoCard(user.getId(), todoCard.getId()))
                .thenReturn(0L);
        when(customFieldRepository.alreadyExistsByFieldNameNotType(todoCard.getId(),
                request.getFieldName(), request.getFieldType())).thenReturn(false);
        when(userService.getUserById(request.getUserId())).thenReturn(user);
        when(todoCardService.getTodoCardById(request.getTodoCardId())).thenReturn(todoCard);

        CustomField newCustomField = new CustomField();
        newCustomField.setUser(user);
        newCustomField.setTodoCard(todoCard);
        newCustomField.setIsActive(false);
        newCustomField.setFieldType(request.getFieldType());
        newCustomField.setFieldName(request.getFieldName());
        newCustomField.setSelectedValue(request.getSelectedValue());

        when(customFieldRepository.save(any(CustomField.class))).thenReturn(newCustomField);

        CustomField returnedCustomField = customFieldService.createCustomField(request);

        Assertions.assertThat(returnedCustomField).isNotNull();
        Assertions.assertThat(returnedCustomField.getFieldType())
                .isEqualTo(newCustomField.getFieldType());
        Assertions.assertThat(returnedCustomField.getFieldName())
                .isEqualTo(newCustomField.getFieldName());
    }

    @Test
    public void CustomFieldService_DeleteCustomField_ThrowForbiddenException() {
        CustomField customField = customFields.getFirst();
        User forbiddenUser = new User();
        forbiddenUser.setId(999L);
        when(customFieldRepository.findById(customField.getId()))
                .thenReturn(Optional.of(customField));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(forbiddenUser);

        Assertions.assertThatThrownBy(() -> {
            customFieldService.deleteCustomField(customField.getId());
        }).isInstanceOf(ForbiddenException.class)
                .hasMessage("Cannot delete a custom field that is not yours");

    }

    @Test
    public void CustomFieldService_DeleteCustomField_ReturnNothing() {
        CustomField customField = customFields.getFirst();

        when(customFieldRepository.findById(customField.getId()))
                .thenReturn(Optional.of(customField));
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        doNothing().when(customFieldRepository).delete(customField);

        customFieldService.deleteCustomField(customField.getId());
    }


}


