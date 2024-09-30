package com.hart.overwatch.customfield;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.checklist.dto.CheckListDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_custom_field_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class CustomFieldRepositoryTest {

    @Autowired
    private CustomFieldRepository customFieldRepository;

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TodoCardRepository todoCardRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private WorkSpace workSpace;

    private TodoCard todoCard;

    private List<CustomField> customFields;


    @BeforeEach
    public void setUp() {
        user = generateUser();
        userRepository.save(user);

        workSpace = generatedWorkSpace(user);
        workSpaceRepository.save(workSpace);

        TodoList todoList = generateTodoList(user, workSpace);

        todoListRepository.save(todoList);

        todoCard = generateTodoCard(user, todoList);
        todoCardRepository.save(todoCard);

        customFields = generateCustomFields(user, todoCard);
        customFieldRepository.saveAll(customFields);
        todoCard.setCustomFields(customFields);


    }

    private List<CustomField> generateCustomFields(User user, TodoCard todoCard) {
        List<CustomField> customFields = new ArrayList<>();
        String[] fieldTypes = {"NUMBER", "DROPDOWN", "TEXT", "CHECKBOX"};
        for (int i = 0; i < fieldTypes.length; i++) {
            CustomField customField = new CustomField();
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
        todoList.setUser(user);
        todoList.setWorkSpace(workSpace);
        todoList.setTitle("title");
        todoList.setIndex(0);

        return todoList;
    }


    private TodoCard generateTodoCard(User user, TodoList todoList) {
        TodoCard todoCard = new TodoCard();
        todoCard.setUser(user);
        todoCard.setTitle("card title");
        todoCard.setIndex(0);
        todoCard.setTodoList(todoList);

        return todoCard;
    }

    private WorkSpace generatedWorkSpace(User user) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setTitle("main");
        workSpace.setBackgroundColor("#000000");
        workSpace.setUser(user);

        return workSpace;
    }

    private User generateUser() {
        User user = new User();
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


    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        customFieldRepository.deleteAll();
        todoCardRepository.deleteAll();
        todoListRepository.deleteAll();
        workSpaceRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void CustomFieldRepository_FindByTodoCardIdAndIsActive_ReturnListOfCustomField() {
        Long todoCardId = todoCard.getId();
        Boolean isActive = true;

        List<CustomField> customFields =
                customFieldRepository.findByTodoCardIdAndIsActive(todoCardId, isActive);

        Assertions.assertThat(customFields).isNotNull();
        Assertions.assertThat(customFields.size()).isEqualTo(2);
    }

    @Test
    public void CustomFieldRepository_FindByTodoCardId_ReturnListOfCustomField() {
        Long todoCardId = todoCard.getId();

        List<CustomField> customFields = customFieldRepository.findByTodoCardId(todoCardId);

        Assertions.assertThat(customFields).isNotNull();
        Assertions.assertThat(customFields.size()).isEqualTo(todoCard.getCustomFields().size());
    }

    @Test
    public void CustomFieldRepository_AlreadyExistsByFieldNameNotType_ReturnBooleanFalse() {
        Long todoCardId = todoCard.getId();
        String fieldName = "fieldname-1";
        String fieldType = "CHECKBOX";

        boolean alreadyExists = customFieldRepository.alreadyExistsByFieldNameNotType(todoCardId,
                fieldName, fieldType);

        Assertions.assertThat(alreadyExists).isFalse();
    }

    @Test
    public void CustomFieldRepository_CountCustomFieldsPerTodoCard_ReturnLongCount() {
        Long userId = user.getId();
        Long todoCardId = todoCard.getId();

        long count = customFieldRepository.countCustomFieldsPerTodoCard(userId, todoCardId);

        Assertions.assertThat(count).isEqualTo(todoCard.getCustomFields().size());
    }
}


