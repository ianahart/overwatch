package com.hart.overwatch.githubtoken;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.github.GitHubService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GitHubTokenServiceTest {


    @InjectMocks
    private GitHubTokenService gitHubTokenService;

    @Mock
    private GitHubTokenRepository gitHubTokenRepository;

    @Mock
    private UserService userService;

    private User user;

    private GitHubToken gitHubToken;


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private GitHubToken createGitHubToken(User user) {
        GitHubToken gitHubTokenEntity = new GitHubToken();
        gitHubTokenEntity.setId(1L);
        gitHubTokenEntity.setAccessToken("dummy_access_token");
        gitHubTokenEntity.setUser(user);

        return gitHubTokenEntity;
    }



    @BeforeEach
    public void setUp() {
        user = createUser();
        gitHubToken = createGitHubToken(user);
    }

    @Test
    public void GitHubTokenService_DeleteGitHubToken_ReturnNothing() {
        Long userId = user.getId();
        doNothing().when(gitHubTokenRepository).deleteByUserId(userId);

        gitHubTokenService.deleteGitHubToken(userId);


        verify(gitHubTokenRepository, times(1)).deleteByUserId(userId);
    }


    @Test
    public void GitHubTokenService_CreateGitHubToken_ThrowBadRequestException() {
        String accessToken = "";
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        doNothing().when(gitHubTokenRepository).deleteByUserId(user.getId());


        Assertions.assertThatThrownBy(() -> {
            gitHubTokenService.createGitHubToken(accessToken);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Could not create Github token without access token");
    }

    @Test
    public void GitHubTokenService_CreateGitHubToken_Return_Long() {
        String accessToken = "dummy_access_token";
        when(userService.getCurrentlyLoggedInUser()).thenReturn(user);
        doNothing().when(gitHubTokenRepository).deleteByUserId(user.getId());

        when(gitHubTokenRepository.save(any(GitHubToken.class))).thenAnswer(invocation -> {
            GitHubToken savedToken = invocation.getArgument(0);
            savedToken.setId(2L);
            return savedToken;
        });
        Long gitHubTokenId = gitHubTokenService.createGitHubToken(accessToken);

        verify(gitHubTokenRepository, times(1)).save(any(GitHubToken.class));

        Assertions.assertThat(gitHubTokenId).isEqualTo(2L);
    }

}


