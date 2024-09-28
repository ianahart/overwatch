package com.hart.overwatch.checklistitem;

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
import com.hart.overwatch.checklist.CheckList;
import com.hart.overwatch.checklist.CheckListRepository;
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
@Sql(scripts = "classpath:reset_check_list_item_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class CheckListItemRepositoryTest {

    @Autowired
    private CheckListItemRepository checkListItemRepository;

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

    private CheckList checkList;

    private List<CheckListItem> checkListItems;

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

        checkList = generateCheckList(user, todoCard);
        checkListRepository.save(checkList);

        checkListItems = generateCheckListItems(user, checkList);
        checkListItemRepository.saveAll(checkListItems);
    }

    private List<CheckListItem> generateCheckListItems(User user, CheckList checkList) {
        List<CheckListItem> checkListItems = new ArrayList<>();
        int toGenerate = 2;

        for (int i = 1; i <= toGenerate; i++) {
            CheckListItem checkListItem = new CheckListItem();
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
        checkListItemRepository.deleteAll();
        checkListRepository.deleteAll();
        todoCardRepository.deleteAll();
        todoListRepository.deleteAll();
        workSpaceRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void CheckListItemRepository_CountCheckListItemsInCheckList_ReturnLongCount() {
        Long checkListId = checkList.getId();

        long count = checkListItemRepository.countCheckListItemsInCheckList(checkListId);

        Assertions.assertThat(count).isEqualTo(2);
    }

    @Test
    public void CheckListItemRepository_CheckListItemExistsByUserIdAndCheckListIdAndTitle_ReturnBooleanTrue() {
        String title = "checklistitem-1";
        Long userId = user.getId();
        Long checkListId = checkList.getId();

        boolean checkListItemExists = checkListItemRepository
                .checkListItemExistsByUserIdAndCheckListIdAndTitle(userId, checkListId, title);

        Assertions.assertThat(checkListItemExists).isTrue();
    }
}


