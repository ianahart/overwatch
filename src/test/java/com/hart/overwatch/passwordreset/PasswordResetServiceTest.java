package com.hart.overwatch.passwordreset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Base64;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.passwordreset.request.ForgotPasswordRequest;
import com.hart.overwatch.passwordreset.request.PasswordResetRequest;
import com.hart.overwatch.passwordreset.response.ForgotPasswordResponse;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Optional;
import javax.crypto.SecretKey;
import com.sendgrid.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    JwtService jwtService;

    @Mock
    private SendGrid sendGrid;

    @Mock
    Configuration configuration;

    @Mock
    UserRepository userRepository;

    @Mock
    Template mockTemplate;

    @Mock
    UserService userService;

    @Mock
    PasswordResetRepository passwordResetRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    private User user;

    private PasswordReset passwordReset;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        Field secretKeyField = PasswordResetService.class.getDeclaredField("secretKey");
        Field senderfField = PasswordResetService.class.getDeclaredField("sender");
        Field defaultTTLField = PasswordResetService.class.getDeclaredField("DEFAULT_TTL");

        secretKeyField.setAccessible(true);
        defaultTTLField.setAccessible(true);
        senderfField.setAccessible(true);

        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        byte[] encodedKey = secretKey.getEncoded();
        String encodedKeyBase64 = Base64.getEncoder().encodeToString(encodedKey);



        secretKeyField.set(passwordResetService, encodedKeyBase64);
        senderfField.set(passwordResetService, "noreply@example.com");
        defaultTTLField.set(passwordResetService, 86400000L);

        token = Jwts.builder().setSubject("john@mail.com").signWith(secretKey).compact();

        boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        passwordReset = new PasswordReset(user, "dummy_code", token);
        passwordReset.setId(1L);
        user.setId(1L);

        passwordReset.setUser(user);
        user.setPasswordResets(Arrays.asList(passwordReset));


    }

    @Test
    public void PasswordResetService_DeleteUserPasswordResetsById_ReturnNothing() {
        doNothing().when(passwordResetRepository).deleteUserPasswordResetsById(user.getId());

        passwordResetService.deleteUserPasswordResetsById(user.getId());


        verify(passwordResetRepository, times(1)).deleteUserPasswordResetsById(user.getId());
    }

    @Test
    public void PasswordResetService_SendForgotPasswordEmail_ReturnForgotPasswordResponse()
            throws IOException, TemplateException {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(user.getEmail());

        when(userRepository.findByEmail(forgotPasswordRequest.getEmail()))
                .thenReturn(Optional.of(user));

        Template mockTemplate = mock(Template.class);
        when(configuration.getTemplate(anyString())).thenReturn(mockTemplate);

        doAnswer(invocation -> {
            Writer writer = invocation.getArgument(1);
            writer.write("mock-email-content");
            return null;
        }).when(mockTemplate).process(any(), any(Writer.class));

        when(sendGrid.api(any(Request.class))).thenReturn(new Response(202, "", null));

        ForgotPasswordResponse response =
                passwordResetService.sendForgotPasswordEmail(forgotPasswordRequest);

        Assertions.assertThat(response.getMessage()).isEqualTo("Email sent successfully...");

        verify(userRepository).findByEmail(forgotPasswordRequest.getEmail());
        verify(configuration).getTemplate(anyString());
        verify(mockTemplate).process(any(), any(Writer.class));
        verify(sendGrid).api(any(Request.class));
    }

    @Test
    public void PasswordResetService_DeletePasswordResetsById_ReturnNothing() {
        doNothing().when(passwordResetRepository).deleteUserPasswordResetsById(user.getId());

        passwordResetService.deletePasswordResetsById(user.getId());

        verify(passwordResetRepository, times(1)).deleteUserPasswordResetsById(user.getId());
    }

    @Test
    public void PasswordResetService_ResetPassword_ReturnNothing() {
        PasswordResetRequest request =
                new PasswordResetRequest(token, "dummy_code", "Test12345%", "Test12345%");
        Claims claims = mock(Claims.class);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(claims.getSubject()).thenReturn(user.getEmail());
        when(jwtService.tokenElapsedDay(request.getToken())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");

        passwordResetService.resetPassword(request);

        verify(passwordResetRepository, times(1)).deleteUserPasswordResetsById(user.getId());
        verify(userService, times(1)).getUserByEmail(claims.getSubject());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    @Test
    public void PasswordResetService_ResetPasswordTokenExpired_ThrowBadRequestException() {
        PasswordResetRequest request =
                new PasswordResetRequest(token, "dummy_code", "Test12345%", "Test12345%");
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(jwtService.tokenElapsedDay(token)).thenReturn(true);
        when(passwordResetRepository.findPasswordResetByUserIdAndToken(user.getId(), token))
                .thenReturn(true);

        Assertions.assertThatThrownBy(() -> {
            passwordResetService.resetPassword(request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessage("Token has expired. Please try resetting your password again");
    }

    @Test
    void PasswordResetService_ResetPasswordPasswordDoNotMatch_ThrowBadRequestException() {
        PasswordResetRequest request =
                new PasswordResetRequest(token, "dummy_code", "Test12345%%", "Test12345%");
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(jwtService.tokenElapsedDay(token)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
            passwordResetService.resetPassword(request);
        }).isInstanceOf(BadRequestException.class).hasMessage("Passwords do not match");
    }

    @Test
    void PasswordResetService_ResetPasswordPasswordValdationFail_ThrowBadRequestException() {
        PasswordResetRequest request =
                new PasswordResetRequest(token, "dummy_code", "Test12345", "Test12345%");
        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(jwtService.tokenElapsedDay(token)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
            passwordResetService.resetPassword(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                "Password must include 1 uppercase, 1 lowercase, 1 digit and 1 special char");
    }
}


