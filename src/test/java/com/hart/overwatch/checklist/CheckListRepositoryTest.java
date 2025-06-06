package com.hart.overwatch.checklist;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
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
@Sql(scripts = "classpath:reset_check_list_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class CheckListRepositoryTest {

    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

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

    private List<CheckList> checkLists;

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

        checkLists = generateCheckLists(user, todoCard);
        checkListRepository.saveAll(checkLists);
    }

    private List<CheckList> generateCheckLists(User user, TodoCard todoCard) {
        LocalDateTime timestamp = LocalDateTime.now();
        int toGenerate = 2;
        List<CheckList> checkLists = new ArrayList<>();

        for (int i = 1; i <= toGenerate; i++) {
            CheckList checkList = new CheckList();
            checkList.setUser(user);
            checkList.setTodoCard(todoCard);
            checkList.setIsCompleted(false);
            checkList.setTitle(String.format("checklist-%d", i));
            checkList.setCreatedAt(timestamp);
            checkList.setUpdatedAt(timestamp);

            checkLists.add(checkList);
        }
        return checkLists;
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


    @Test
    public void CheckListRepository_GetCheckListsByTodoCardId_ReturnPageOfCheckListDto() {
        Long todoCardId = todoCard.getId();
        Pageable pageable = PageRequest.of(0, 10);

        Page<CheckListDto> page =
                checkListRepository.getCheckListsByTodoCardId(todoCardId, pageable);

        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page.getContent().size()).isEqualTo(2);
    }

    @Test
    public void CheckListRepository_CountCheckListsInTodoCard_ReturnLongCount() {
        Long todoCardId = todoCard.getId();

        long count = checkListRepository.countCheckListsInTodoCard(todoCardId);

        Assertions.assertThat(count).isEqualTo(checkLists.size());
    }

    @Test
    public void CheckListRepository_CheckListsExistsByTitleAndUserIdAndTodoCardId_ReturnBooleanTrue() {
        Long userId = user.getId();
        Long todoCardId = todoCard.getId();
        String title = checkLists.get(0).getTitle();

        boolean exists = checkListRepository.checkListExistsByTitleAndUserIdAndTodoCardId(userId,
                todoCardId, title);

        Assertions.assertThat(exists).isTrue();
    }
}


