package com.hart.overwatch.user;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentCaptor.forClass;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        Boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());

        user.setId(1L);
    }

    @Test
    public void UserService_UserExistsByEmail_ReturnBoolean() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        boolean userExistsByEmail = userService.userExistsByEmail(user.getEmail());

        Assertions.assertThat(userExistsByEmail).isTrue();
    }

    @Test
    public void UserService_GetUserByEmail_ReturnUser() {
       when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User returnedUser = userService.getUserByEmail(user.getEmail());

        Assertions.assertThat(returnedUser).isNotNull();
        Assertions.assertThat(returnedUser.getId()).isEqualTo(user.getId());
    }

    @Test
    public void UserService_GetUserByEmail_ThrowNotFoundException() {
        when(userRepository.findByEmail("doesnotexist@mail.com")).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> userService.getUserByEmail("doesnotexist@mail.com"))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("User with that email does not exist");
    }

    @Test
    public void UserService_GetUserById_ReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User returnedUser = userService.getUserById(user.getId());

        Assertions.assertThat(returnedUser).isNotNull();
        Assertions.assertThat(returnedUser.getId()).isEqualTo(user.getId());

    }

    @Test
    public void UserService_GetUserById_ThrowNotFoundException() {
       when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(null));

       Assertions.assertThatThrownBy(() -> userService.getUserById(2L))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(String.format("A user with the id %d does not exist", 2L));
    }

    @Test
    public void UserService_GetCurrentlyLoggedInUser_ReturnUser() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        User returnedUser = userService.getCurrentlyLoggedInUser();

        Assertions.assertThat(returnedUser.getEmail()).isEqualTo(user.getEmail());
    }

}
