package com.hart.overwatch.todocard;

import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsEqual;
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
import com.hart.overwatch.todocard.dto.TodoCardDto;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.todolist.dto.TodoListDto;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.workspace.WorkSpace;
import java.util.List;
import java.util.ArrayList;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_todo_list_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TodoCardRepositoryTest {

    @Autowired
    private TodoCardRepository todoCardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoListRepository todoListRepository;

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

        todoList = new TodoList(user, workSpace, "todo list title", 0);
        todoListRepository.save(todoList);
        List<TodoCard> todoCards = createTodoCards(user, todoList);
        todoList.setTodoCards(todoCards);
        todoListRepository.save(todoList);
        todoCardRepository.saveAll(todoCards);



    }


    private List<TodoCard> createTodoCards(User user, TodoList todoList) {
        int numOfCards = 3;
        List<TodoCard> todoCards = new ArrayList<>();
        for (int i = 1; i <= numOfCards; i++) {
            TodoCard todoCard = new TodoCard();
            todoCard.setId(Long.valueOf(i));
            todoCard.setUser(user);
            todoCard.setTodoList(todoList);
            todoCard.setTitle(String.format("Card-%d", i));
            todoCards.add(todoCard);
        }
        return todoCards;
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        todoCardRepository.deleteAll();
        todoListRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TodoCardRepository_RetrieveTodoCards_ReturnTodoCardDto() {
        List<TodoCardDto> todoCardDtos =
                todoCardRepository.retrieveTodoCards(todoList.getId(), user.getId());

        Assertions.assertThat(todoCardDtos).isNotNull();
        Assertions.assertThat(todoCardDtos.size()).isEqualTo(3);
        Assertions.assertThat(todoCardDtos.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(todoCardDtos.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(todoCardDtos.get(2).getId()).isEqualTo(3L);
    }

    @Test
    public void TodoCardRepository_CountTodoCardsInTodoLIst_ReturnLongCount() {
        long count = todoCardRepository.countTodoCardsInTodoList(todoList.getId(), user.getId());

        Assertions.assertThat(count).isEqualTo(todoList.getTodoCards().size());
    }
}


