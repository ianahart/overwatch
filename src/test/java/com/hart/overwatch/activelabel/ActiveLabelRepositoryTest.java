package com.hart.overwatch.activelabel;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.activelabel.dto.ActiveLabelDto;
import com.hart.overwatch.label.Label;
import com.hart.overwatch.label.LabelRepository;
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
@Sql(scripts = "classpath:reset_active_label_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ActiveLabelRepositoryTest {

    @Autowired
    private ActiveLabelRepository activeLabelRepository;

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TodoCardRepository todoCardRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private WorkSpace workSpace;

    private TodoCard todoCard;

    private List<Label> labels;

    private ActiveLabel activeLabel;

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

        labels = generateLabels(user, workSpace);
        labelRepository.saveAll(labels);

        activeLabel = generateActiveLabel(labels.get(0), todoCard);
        activeLabelRepository.save(activeLabel);
    }

    private TodoList generateTodoList(User user, WorkSpace workSpace) {
        TodoList todoList = new TodoList();
        todoList.setUser(user);
        todoList.setWorkSpace(workSpace);
        todoList.setTitle("title");
        todoList.setIndex(0);

        return todoList;
    }

    private ActiveLabel generateActiveLabel(Label label, TodoCard todoCard) {
        ActiveLabel activeLabel = new ActiveLabel();
        activeLabel.setLabel(label);
        activeLabel.setTodoCard(todoCard);

        return activeLabel;
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

    private List<Label> generateLabels(User user, WorkSpace workSpace) {
        LocalDateTime timestamp = LocalDateTime.now();
        String[] titles = new String[] {"priority", "warning"};
        List<Label> labels = new ArrayList<>();
        int toGenerate = 2;
        for (int i = 1; i <= toGenerate; i++) {
            Label label = new Label();
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

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        activeLabelRepository.deleteAll();
        todoCardRepository.deleteAll();
        todoListRepository.deleteAll();
        workSpaceRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
        activeLabelRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void ActiveLabelRepository_GetActiveLabels_ReturnListOfActiveLabelDtos() {
        Long todoCardId = todoCard.getId();

        List<ActiveLabelDto> activeLabelDtos = activeLabelRepository.getActiveLabels(todoCardId);

        Assertions.assertThat(activeLabelDtos).isNotNull();
        Assertions.assertThat(activeLabelDtos.size()).isEqualTo(1);
        ActiveLabelDto activeLabelDto = activeLabelDtos.get(0);
        Assertions.assertThat(activeLabelDto.getTodoCardId())
                .isEqualTo(activeLabel.getTodoCard().getId());
        Assertions.assertThat(activeLabelDto.getLabelId())
                .isEqualTo(activeLabel.getLabel().getId());
    }

}


