package com.hart.overwatch.authentication;

import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.authentication.request.LoginRequest;
import com.hart.overwatch.authentication.request.RegisterRequest;
import com.hart.overwatch.authentication.response.LoginResponse;
import com.hart.overwatch.authentication.response.RegisterResponse;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.user.dto.UserDto;
import com.hart.overwatch.util.MyUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.test.context.ActiveProfiles;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.phone.PhoneService;
import com.hart.overwatch.profile.ProfileService;
import com.hart.overwatch.refreshtoken.RefreshToken;
import com.hart.overwatch.refreshtoken.RefreshTokenService;
import com.hart.overwatch.setting.SettingService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.token.TokenService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {


    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileService profileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    TokenRepository tokenRepository;

    @Mock
    RefreshTokenService refreshTokenService;

    @Mock
    TokenService tokenService;

    @Mock
    SettingService settingService;

    @Mock
    PhoneService phonesService;

    @Mock
    private UserService userService;

    private User user;

    private String token;

    private RegisterRequest registerRequest;

    private User createuUser() {
        Boolean loggedIn = false;
        Setting setting = new Setting();
        Profile profile = new Profile();
        User user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", setting);
        user.setId(1L);
        profile.setId(1L);
        setting.setId(1L);
        profile.setAvatarUrl("https://www.overwatch.com/photos/photo.jpeg");
        profile.setEmail("john@mail.com");
        profile.setContactNumber("4444444444");
        profile.setBio("This is a bio");
        user.setProfile(profile);
        profile.setUser(user);
        user.setSetting(setting);
        setting.setUser(user);

        return user;
    }

    private String createAuthToken(String subject) {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Jwts.builder().setSubject(subject).signWith(secretKey).compact();

    }

    private RegisterRequest createRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("jane@mail.com");
        request.setFirstName("jane");
        request.setLastName("doe");
        request.setPassword("Test12345%");
        request.setConfirmPassword("Test12345%");
        request.setRole("USER");
        return request;
    }

    @BeforeEach
    void setUp() throws Exception {
        user = createuUser();
        token = createAuthToken(user.getEmail());
        registerRequest = createRegisterRequest();

        Field defaultTTLField = AuthenticationService.class.getDeclaredField("DEFAULT_TTL");
        defaultTTLField.setAccessible(true);
        defaultTTLField.set(authenticationService, 86400000L);
    }

    @Test
    public void AuthenticationService_RegisterEmailExists_ThrowBadRequestException() {
        when(userService.userExistsByEmail(registerRequest.getEmail())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            authenticationService.register(registerRequest);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("A user with this email already exists");
    }

    @Test
    public void AuthenticationService_RegisterPasswordValidationFail_ThrowBadRequestException() {
        when(userService.userExistsByEmail(registerRequest.getEmail())).thenReturn(false);
        registerRequest.setPassword("not_a_valid_password");

        Assertions.assertThatThrownBy(() -> {
            authenticationService.register(registerRequest);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                "Password must contain 1 letter, 1 number, 1 special char, and 1 uppercase letter");
    }

    @Test
    public void AuthenticationService_RegisterPasswordsDoNotMatch_ThrowBadRequestException() {
        when(userService.userExistsByEmail(registerRequest.getEmail())).thenReturn(false);
        registerRequest.setPassword("Test12345%%");

        Assertions.assertThatThrownBy(() -> {
            authenticationService.register(registerRequest);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                "Passwords do not match");
    }

    @Test
    public void AuthenticationService_Register_ReturnRegisterResponse() {
        when(userService.userExistsByEmail(registerRequest.getEmail())).thenReturn(false);
        Profile newProfile = new Profile();
        Setting newSetting = new Setting();
        newProfile.setId(2L);
        newSetting.setId(2L);

        when(profileService.createProfile()).thenReturn(newProfile);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded_password");
        when(settingService.createSetting()).thenReturn(newSetting);

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setEmail(MyUtil.capitalize(registerRequest.getFirstName()));
        newUser.setEmail(MyUtil.capitalize(registerRequest.getLastName()));
        newUser.setFullName(String.format("%s %s", MyUtil.capitalize(registerRequest.getFirstName()), MyUtil.capitalize(registerRequest.getLastName())));
        newUser.setPassword("encoded_password");
        newUser.setSetting(new Setting());
        newUser.setProfile(new Profile());
        newUser.setRole(Role.USER);


        when(userRepository.save(any(User.class))).thenReturn(newUser);
        RegisterResponse response = authenticationService.register(registerRequest);

        Assertions.assertThat(response.getMessage()).isEqualTo("User created");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void AuthenticationService_LoginBadCredentials_ThrowForbiddenException() {
        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());

        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ForbiddenException exception = Assertions.catchThrowableOfType(
                () -> authenticationService.login(loginRequest), ForbiddenException.class);

        Assertions.assertThat(exception).isInstanceOf(ForbiddenException.class)
                .hasMessage("Credentials are invalid");
    }

    @Test
    public void AuthenticationService_Login_ReturnLoginResponse() {
        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        String token = "jwtToken";
        when(jwtService.generateToken(user, 86400000L)).thenReturn(token);

        doNothing().when(tokenService).revokeAllUserTokens(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(refreshTokenService.generateRefreshToken(user.getId())).thenReturn(
                new RefreshToken("refreshToken", Instant.now().plusMillis(86400000L), user));

        LoginResponse response = authenticationService.login(loginRequest);

        Assertions.assertThat(response).isNotNull();

        UserDto userDto = response.getUser();
        Assertions.assertThat(userDto).isNotNull();
        Assertions.assertThat(userDto.getId()).isEqualTo(user.getId());
        Assertions.assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        Assertions.assertThat(userDto.getLastName()).isEqualTo(user.getLastName());

        Assertions.assertThat(response.getToken()).isEqualTo(token);
        Assertions.assertThat(response.getRefreshToken()).isEqualTo("refreshToken");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(user.getEmail());
        verify(jwtService).generateToken(user, 86400000L);
        verify(tokenService).revokeAllUserTokens(user);
        verify(userRepository).save(any(User.class));
        verify(refreshTokenService).generateRefreshToken(user.getId());
    }
}


