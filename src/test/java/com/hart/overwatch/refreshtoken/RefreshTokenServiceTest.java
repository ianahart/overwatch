package com.hart.overwatch.refreshtoken;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentCaptor.forClass;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;
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
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    List<RefreshToken> refreshTokens = new ArrayList<>();

    @BeforeEach
    void setUp() {
        boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        user.setId(1L);

        int numOfTokensToGenerate = 2;

        for (int i = 0; i < numOfTokensToGenerate; i++) {
            String tokenValue = "token" + UUID.randomUUID().toString();
            refreshTokens.add(new RefreshToken(Long.valueOf(i + 1), tokenValue,
                    Instant.now().plusMillis(86400000 * 12), user));
        }
        user.setRefreshTokens(refreshTokens);
    }

    @Test
    public void RefreshTokenService_GenerateRefreshToken_ReturnRefreshToken() {
        String tokenValue = "token" + UUID.randomUUID().toString();
        RefreshToken refreshTokenToSave =
                new RefreshToken(3L, tokenValue, Instant.now().plusMillis(86400000 * 12), user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshTokenToSave);

        RefreshToken returnedRefreshToken = refreshTokenService.generateRefreshToken(user.getId());

        Assertions.assertThat(returnedRefreshToken).isNotNull();
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    public void RefreshTokenService_RevokeAllUserRefreshTokens_ReturnNothing() {
        when(refreshTokenRepository.findAllUserRefreshTokens(user.getId())).thenReturn(refreshTokens);

        refreshTokenService.revokeAllUserRefreshTokens(user);

        verify(refreshTokenRepository, times(1)).findAllUserRefreshTokens(user.getId());
        verify(refreshTokenRepository, times(1)).deleteById(1L);
        verify(refreshTokenRepository, times(1)).deleteById(2L);

    }

    @Test
    public void RefreshTokenService_VerifyRefreshToken_ReturnRefreshToken() {
        String tokenValue = "token" + UUID.randomUUID().toString();
        RefreshToken refreshToken =
                new RefreshToken(3L, tokenValue, Instant.now().plusMillis(86400000 * 12), user);

        when(refreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken()))
                .thenReturn(Optional.of(refreshToken));


        RefreshToken returnedRefreshToken =
                refreshTokenService.verifyRefreshToken(refreshToken.getRefreshToken());

        verify(refreshTokenRepository, times(1)).findByRefreshToken(refreshToken.getRefreshToken());
        Assertions.assertThat(returnedRefreshToken).isNotNull();
    }


}


