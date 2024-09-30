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
    public void CustomFieldService_GetCustomFieldById_ThrowNotFoundException() {
        Long nonExistentCustomFieldId = 999L;

        when(customFieldRepository.findById(nonExistentCustomFieldId))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            customFieldService.getCustomFieldById(nonExistentCustomFieldId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("Could not find custom field with id %d", nonExistentCustomFieldId));
    }

}


