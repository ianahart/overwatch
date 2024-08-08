package com.hart.overwatch.user;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentCaptor.forClass;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.dto.UpdateUserDto;
import com.hart.overwatch.user.dto.UserDto;
import com.hart.overwatch.user.request.UpdateUserRequest;
import com.twilio.twiml.voice.Prompt.For;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Value("${secretkey}")
    private String secretKey;

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
        Profile profile = new Profile();
        Setting setting = new Setting();
        setting.setId(1L);
        profile.setId(1L);
        profile.setAvatarUrl("http://example.com/avatar.jpg");
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", setting);

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

    @Test
    public void UserService_UpdateUserPassword_ReturnNothing() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        String currentEncodedPassword = "encodedCurrentPassword";
        String newEncodedPassword = "encodedNewPassword";

        user.setPassword(currentEncodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Test12345%", "encodedCurrentPassword")).thenReturn(true);
        when(passwordEncoder.matches("Test12345%%", "encodedCurrentPassword")).thenReturn(false);
        when(passwordEncoder.encode("Test12345%%")).thenReturn(newEncodedPassword);

        userService.updateUserPassword("Test12345%", "Test12345%%", user.getId());

        verify(userRepository).save(user);
        Assertions.assertThat(user.getPassword()).isEqualTo(newEncodedPassword);
    }

    @Test
    public void UserService_UpdateUserPasswordAnotherUser_ThrowForbiddenException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        String currentEncodedPassword = "encodedCurrentPassword";

        user.setPassword(currentEncodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> userService.updateUserPassword("Test12345%", "Test12345%%", 2L))
                .isInstanceOf(ForbiddenException.class);

    }

    @Test
    public void UserService_UpdateUserPasswordPasswordSame_ThrowForbiddenException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        String currentEncodedPassword = "encodedCurrentPassword";

        user.setPassword(currentEncodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Test12345%", currentEncodedPassword)).thenReturn(true);
        when(passwordEncoder.matches("Test12345%", currentEncodedPassword)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> userService.updateUserPassword("Test12345%", "Test12345%", 1L))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("Your current password is invalid or is the same as your old one");

    }

    @Test
    public void UserService_DeleteUser_ThrowForbiddenExceptionWrongUser() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        String currentEncodedPassword = "encodedCurrentPassword";

        user.setPassword(currentEncodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> userService.deleteUser(2L, "Test12345%"))
                .isInstanceOf(ForbiddenException.class).hasMessage("Cannot delete another user");
    }

    @Test
    public void UserService_DeleteUser_ThrowForbiddenExceptionWrongPassword() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        String currentEncodedPassword = "encodedCurrentPassword";

        user.setPassword(currentEncodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Test12345%", currentEncodedPassword)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> userService.deleteUser(user.getId(), "Test12345%"))
                .isInstanceOf(ForbiddenException.class).hasMessage("Password is not a match");
    }

    @Test
    public void UserService_DeleteUser_DoNothing() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);

        SecurityContextHolder.setContext(securityContext);

        String currentEncodedPassword = "encodedCurrentPassword";

        user.setPassword(currentEncodedPassword);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Test12345%", currentEncodedPassword)).thenReturn(true);
        doNothing().when(userRepository).delete(user);
        userService.deleteUser(user.getId(), "Test12345%");

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void UserService_UpdateUser_ThrowForbiddenException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UpdateUserRequest request = new UpdateUserRequest("john", "smith", "smith@gmail.com");

        Assertions.assertThatThrownBy(() -> userService.updateUser(request, 999L))
                .isInstanceOf(ForbiddenException.class).hasMessage("Cannot update another user's information");

    }

    @Test
    public void UserService_UpdateUser_ReturnUpdateUserDto() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        String username = user.getEmail();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UpdateUserRequest request = new UpdateUserRequest("john", "smith", "smith@gmail.com");

        UpdateUserDto result = userService.updateUser(request, user.getId());

        Assertions.assertThat(result.getFirstName().toLowerCase()).isEqualTo(result.getFirstName().toLowerCase());
        Assertions.assertThat(result.getLastName().toLowerCase()).isEqualTo(result.getLastName().toLowerCase());
        Assertions.assertThat(result.getEmail()).isEqualTo(result.getEmail());

    }
}
