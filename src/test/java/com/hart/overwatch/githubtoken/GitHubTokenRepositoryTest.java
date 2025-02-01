package com.hart.overwatch.githubtoken;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_github_token_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class GitHubTokenRepositoryTest {

    @Autowired
    private GitHubTokenRepository gitHubTokenRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private GitHubToken gitHubToken;


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private GitHubToken createGitHubToken(User user) {
        GitHubToken gitHubTokenEntity = new GitHubToken();
        gitHubTokenEntity.setAccessToken("dummy_access_token");
        gitHubTokenEntity.setUser(user);

        gitHubTokenRepository.save(gitHubTokenEntity);
        return gitHubTokenEntity;
    }



    @BeforeEach
    public void setUp() {
        user = createUser();
        gitHubToken = createGitHubToken(user);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        gitHubTokenRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void GitHubTokenRepository_DeleteByUserId_ReturnNothing() {

        Assertions.assertThat(gitHubTokenRepository.findAll()).isNotEmpty();

        gitHubTokenRepository.deleteByUserId(user.getId());

        Assertions.assertThat(gitHubTokenRepository.findAll().isEmpty());
    }
}


