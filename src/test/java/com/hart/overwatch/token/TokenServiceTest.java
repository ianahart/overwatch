package com.hart.overwatch.token;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentCaptor.forClass;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserService userService;

    private User user;

    List<Token> tokens = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        int numOfTokensToGenerate = 2;

        for (int i = 0; i < numOfTokensToGenerate; i++) {
            String tokenValue = "token" + i;
            tokens.add(new Token(tokenValue, TokenType.BEARER, false, false, user));
        }
        user.setTokens(tokens);
    }


    @Test
    public void TokenService_SaveTokenWithUser_ReturnNothing() {
        String tokenStr = "token3";

        ArgumentCaptor<Token> tokenCaptor = forClass(Token.class);

        tokenService.saveTokenWithUser(tokenStr, user);

        verify(tokenRepository).save(tokenCaptor.capture());

        Token capturedToken = tokenCaptor.getValue();

        Assertions.assertThat(capturedToken).isNotNull();
        Assertions.assertThat(capturedToken.getToken()).isEqualTo(tokenStr);
        Assertions.assertThat(capturedToken.getUser()).isEqualTo(user);
        Assertions.assertThat(capturedToken.getTokenType()).isEqualTo(TokenType.BEARER);
        Assertions.assertThat(capturedToken.getRevoked()).isFalse();
        Assertions.assertThat(capturedToken.getExpired()).isFalse();
    }


    @Test
    public void TokenService_RevokeAllUserTokens_Success() {
        when(tokenRepository.findAllValidTokens(user.getId())).thenReturn(tokens);
        tokenService.revokeAllUserTokens(user);

        verify(tokenRepository).saveAll(any());

        Assertions.assertThat(tokens).hasSize(2);
        tokens.forEach(token -> {
            Assertions.assertThat(token.getExpired()).isTrue();
            Assertions.assertThat(token.getRevoked()).isTrue();
        });
    }

    @Test
    public void TokenService_DeleteAllExpiredTokens_ReturnNothing() {
        tokens.add(new Token("token4", TokenType.BEARER, true, true, user));

        when(tokenRepository.deleteAllExpiredTokens()).thenReturn(Arrays.asList(tokens.get(0)));

        tokenService.deleteAllExpiredTokens();

        verify(tokenRepository, times(1)).delete(any(Token.class));

    }
}


