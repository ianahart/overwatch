package com.hart.overwatch.user;

import java.util.Optional;
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

import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_user_sequences.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    public void setUp() {
        Boolean loggedIn = false;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        userRepository.saveAndFlush(user);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void UserRepository_CountUsers_ReturnLongCount() {
        Long userCount = userRepository.countUsers();

        Assertions.assertThat(userCount).isEqualTo(1L);

    }

    @Test
    public void UserRepository_FindByEmail_ReturnUser() {
        Optional<User> returnedUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(returnedUser).isNotEmpty();
        Assertions.assertThat(returnedUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void UserRepository_UpdateLoggedIn_ReturnNothing() {

        userRepository.updateLoggedIn(1L, true);

        userRepository.flush();

        Optional<User> returnedUser = userRepository.findById(1L);

        System.out.println(returnedUser.get().getLoggedIn());
        Assertions.assertThat(returnedUser).isPresent();
    }
}
