package com.hart.overwatch.refreshtoken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import java.time.Instant;
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
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_refresh_token_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private List<RefreshToken> refreshTokens = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        userRepository.save(user);
        int numOfTokensToGenerate = 2;

        for (int i = 0; i < numOfTokensToGenerate; i++) {
            String tokenValue = "token" + UUID.randomUUID().toString();
            refreshTokens.add(
                    new RefreshToken(tokenValue, Instant.now().plusMillis(86400000 * 12), user));
        }
        refreshTokenRepository.saveAll(refreshTokens);
        user.setRefreshTokens(refreshTokens);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void RefreshTokenRepository_FindByRefreshToken_ReturnRefreshToken() {

        Optional<RefreshToken> refreshToken =
                refreshTokenRepository.findByRefreshToken(refreshTokens.get(0).getRefreshToken());

        List<String> existingTokens = refreshTokens.stream().map(v -> v.getRefreshToken()).toList();

        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(refreshToken.get().getRefreshToken()).isIn(existingTokens);
    }

    @Test
    public void RefreshTokenRepository_FindAllUserRefreshTokens_ReturnListOfRefreshTokens() {
        List<RefreshToken> foundRefreshTokens =
                refreshTokenRepository.findAllUserRefreshTokens(user.getId());

        Assertions.assertThat(foundRefreshTokens).isNotNull();

    }

    @Test
    public void RefreshTokenRepository_DeleteByUser_ReturnInt() {
        int recordsDeleted = refreshTokenRepository.deleteByUser(user);

        Assertions.assertThat(recordsDeleted).isGreaterThan(0);
        Assertions.assertThat(recordsDeleted).isEqualTo(user.getRefreshTokens().size());
    }
}


