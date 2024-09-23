package com.hart.overwatch.todolist;

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
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceRepository;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_todo_list_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TodoListRepositoryTest {

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkSpaceRepository workSpaceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private WorkSpace workSpace;

    private TodoList todoList;

    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        userRepository.save(user);

        workSpace = new WorkSpace("main", "#000000", user);

        workSpaceRepository.save(workSpace);

        todoList = new TodoList(user, workSpace, "todo list title", 0);

        todoListRepository.save(todoList);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        todoListRepository.deleteAll();
        workSpaceRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }
}


