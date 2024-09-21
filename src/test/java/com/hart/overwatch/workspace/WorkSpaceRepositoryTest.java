package com.hart.overwatch.workspace;

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
import com.hart.overwatch.workspace.dto.WorkSpaceDto;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_workspace_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class WorkSpaceRepositoryTest {

    @Autowired
    private WorkSpaceRepository workSpaceRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;
    private WorkSpace workSpace;

    @BeforeEach
    public void setUp() {
        Boolean loggedIn = false;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        userRepository.save(user);

        workSpace = new WorkSpace("main", "#000000", user);

        workSpaceRepository.save(workSpace);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        workSpaceRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void WorkSpaceRepository_GetLatestWorkSpaceByUserId_ReturnPageOfWorkSpaceDto() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<WorkSpaceDto> result =
                workSpaceRepository.getLatestWorkSpaceByUserId(pageable, user.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent().size()).isEqualTo(1);
    }

    @Test
    public void WorkSpaceRepository_AleadyExistsByTitleAndUserId_ReturnBoolean() {
        boolean exists = workSpaceRepository.alreadyExistsByTitleAndUserId(workSpace.getTitle(),
                user.getId());
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void WorkSpaceRepository_CountWorkSpacesByUserId_ReturnLongCount() {
        long count = workSpaceRepository.countWorkSpacesByUserId(user.getId());
        Assertions.assertThat(count).isEqualTo(1L);
    }
}

