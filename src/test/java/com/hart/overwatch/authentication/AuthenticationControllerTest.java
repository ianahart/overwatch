package com.hart.overwatch.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hart.overwatch.authentication.request.LoginRequest;
import com.hart.overwatch.authentication.request.RegisterRequest;
import com.hart.overwatch.authentication.response.LoginResponse;
import com.hart.overwatch.authentication.response.RegisterResponse;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.passwordreset.PasswordResetService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.refreshtoken.RefreshTokenService;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.token.TokenService;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.user.dto.UserDto;
import com.hart.overwatch.util.MyUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;

@ActiveProfiles("test")
@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    TokenService tokenService;

    @MockBean
    PasswordResetService passwordResetService;

    @MockBean
    UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void AuthenticationController_Register_ReturnRegsiterResponse() throws Exception {
        when(authenticationService.register(registerRequest)).thenReturn(new RegisterResponse("User created"));

        ResultActions response =
                mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")));
    }

    @Test
    public void AuthenticationController_Login_ReturnLoginResponse() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(MyUtil.capitalize(user.getFirstName()));
        userDto.setLastName(MyUtil.capitalize(user.getLastName()));
        userDto.setRole(user.getRole());
        userDto.setAbbreviation(user.getAbbreviation());
        userDto.setLoggedIn(user.getLoggedIn());
        userDto.setProfileId(user.getProfile().getId());
        userDto.setAvatarUrl(user.getProfile().getAvatarUrl());
        userDto.setFullName(String.format("%s %s", userDto.getFirstName(), userDto.getLastName()));
        userDto.setSettingId(user.getSetting().getId());
        userDto.setSlug(user.getSlug());

        String refreshToken = "refreshToken";
        LoginResponse loginResponse = new LoginResponse(userDto, token, refreshToken);
        LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());

        when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        ResultActions response =
                mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", CoreMatchers.is(token)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken",
                        CoreMatchers.is(refreshToken)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id",
                        CoreMatchers.is(user.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email",
                        CoreMatchers.is(user.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName",
                        Matchers.equalToIgnoringCase(user.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.lastName",
                        Matchers.equalToIgnoringCase(user.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.loggedIn",
                        CoreMatchers.is(user.getLoggedIn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.profileId",
                        CoreMatchers.is(user.getProfile().getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.settingId",
                        CoreMatchers.is(user.getSetting().getId().intValue())));

    }

    //
    // @Test
    // public void ChatMessageControllerTest_GetAllChatMessages_ReturnGetAllChatMessagesResponse()
    // throws Exception {
    // when(chatMessageService.getChatMessages(connection.getId())).thenReturn(List.of(chatMessageDto));
    //
    // ResultActions response = mockMvc
    // .perform(get("/api/v1/chat-messages")
    // .contentType(MediaType.APPLICATION_JSON)
    // .param("connectionId", String.valueOf(connection.getId())));
    //
    // response.andExpect(MockMvcResultMatchers.status().isOk())
    // .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("success")))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id",
    // CoreMatchers.is(chatMessageDto.getId().intValue())))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName",
    // CoreMatchers.is(chatMessageDto.getFirstName())))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName",
    // CoreMatchers.is(chatMessageDto.getLastName())))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].text",
    // CoreMatchers.is(chatMessageDto.getText())))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].avatarUrl",
    // CoreMatchers.is(chatMessageDto.getAvatarUrl())))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].connectionId",
    // CoreMatchers.is(chatMessageDto.getConnectionId().intValue())))
    // .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId",
    // CoreMatchers.is(chatMessageDto.getUserId().intValue())));
    //
    // }
    //
}


