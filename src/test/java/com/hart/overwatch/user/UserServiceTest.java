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
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;

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

    // public boolean userExistsByEmail(String email) {
    // Optional<User> user = this.userRepository.findByEmail(email);
    // return user.isPresent();
    // }
    //

}
