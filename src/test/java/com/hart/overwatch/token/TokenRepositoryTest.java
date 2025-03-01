package com.hart.overwatch.token;

import java.util.ArrayList;
import java.util.List;
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
import static org.mockito.Mockito.when;
import org.springframework.test.context.bean.override.mockito.MockitoBean;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_token_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private List<Token> tokens = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        userRepository.save(user);
        int numOfTokensToGenerate = 2;

        for (int i = 0; i < numOfTokensToGenerate; i++) {
            String tokenValue = "token" + i;
            when(jwtService.generateToken(user, 86400000L)).thenReturn(tokenValue);
            tokens.add(new Token(tokenValue, TokenType.BEARER, false, false, user));
        }
        user.setTokens(tokens);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        userRepository.deleteAll();
        tokenRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TokenRepository_FindAllValidTokens_ReturnListOfTokens() {

        List<Token> validTokens = tokenRepository.findAllValidTokens(user.getId());

        Assertions.assertThat(validTokens.size()).isEqualTo(2);
    }

    @Test
    public void TokenRepository_FindByToken_ReturnToken() {

        Optional<Token> returnedToken = tokenRepository.findByToken("token0");

        Assertions.assertThat(returnedToken.isPresent());
    }

    @Test
    void TokenRepository_DeleteAllExpiredTokens_ReturnExpiredTokens() {
        tokens.get(0).setExpired(true);
        tokens.get(0).setRevoked(true);
        tokenRepository.save(tokens.get(0));

        List<Token> expiredTokens = tokenRepository.deleteAllExpiredTokens();

        Assertions.assertThat(expiredTokens.size()).isEqualTo(1);
    }
}


