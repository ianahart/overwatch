package com.hart.overwatch.activity;

import java.time.LocalDateTime;
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
import com.hart.overwatch.activity.dto.ActivityDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.todocard.TodoCard;
import com.hart.overwatch.todocard.TodoCardRepository;
import com.hart.overwatch.todolist.TodoList;
import com.hart.overwatch.todolist.TodoListRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.workspace.WorkSpace;
import com.hart.overwatch.workspace.WorkSpaceRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_activity_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    TodoCardRepository todoCardRepository;

    @Autowired
    WorkSpaceRepository workSpaceRepository;

    @Autowired
    TodoListRepository todoListRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Activity activity;

    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        userRepository.save(user);

        WorkSpace workSpace = new WorkSpace("work space title", "#0000FF", user);
        TodoList todoList = new TodoList(user, workSpace, "todo list title", 0);
        TodoCard todoCard = new TodoCard("todo card title", 0, user, todoList);

        workSpaceRepository.save(workSpace);
        todoListRepository.save(todoList);
        todoCardRepository.save(todoCard);

        activity = new Activity("some activity text", user, todoCard);

        activityRepository.save(activity);
    }


    @Test
    public void ActivityRepository_CountActivitiesbyuserIdAndCreatedAfter_ReturnInt() {
        LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10);
        Long userId = user.getId();

        int count = activityRepository.countActivitiesByUserIdAndCreatedAtAfter(userId, createdAt);

        Assertions.assertThat(count).isGreaterThan(0);
    }

    @Test
    public void ActivityRepository_GetAllActivitiesByTodoCardId_ReturnPageOfActivityDto() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<ActivityDto> result = activityRepository
                .getAllActivitiesByTodoCardId(activity.getTodoCard().getId(), pageable);

        Assertions.assertThat(result.getContent().size()).isEqualTo(1);

        ActivityDto activityDto = result.getContent().get(0);
        Assertions.assertThat(activityDto.getId()).isEqualTo(activity.getId());
    }
}


