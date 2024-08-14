package com.hart.overwatch.passwordreset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.passwordreset.request.ForgotPasswordRequest;
import com.hart.overwatch.passwordreset.response.ForgotPasswordResponse;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Mock
    JwtService jwtService;

    @Mock
    Configuration configuration;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordResetRepository passwordResetRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    private User user;

    private PasswordReset passwordReset;

    @BeforeEach
    void setUp() throws Exception {
        boolean loggedIn = true;
        user = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        passwordReset = new PasswordReset(user, "dummy_code", "dummy_token");
        passwordReset.setId(1L);
        user.setId(1L);

        passwordReset.setUser(user);
        user.setPasswordResets(Arrays.asList(passwordReset));

        Field senderfField = PasswordResetService.class.getDeclaredField("sender");
        Field defaultTTLField = PasswordResetService.class.getDeclaredField("DEFAULT_TTL");
        defaultTTLField.setAccessible(true);
        senderfField.setAccessible(true);
        senderfField.set(passwordResetService, "noreply@example.com");
        defaultTTLField.set(passwordResetService, 86400000L);

    }

    @Test
    public void PasswordResetService_DeleteUserPasswordResetsById_ReturnNothing() {
        doNothing().when(passwordResetRepository).deleteUserPasswordResetsById(user.getId());

        passwordResetService.deleteUserPasswordResetsById(user.getId());


        verify(passwordResetRepository, times(1)).deleteUserPasswordResetsById(user.getId());
    }

    @Test
    public void PasswordResetService_SendForgotPasswordEmail_ReturnForgotPasswordResponse()
            throws MessagingException, IOException, TemplateException {
        ForgotPasswordRequest request = new ForgotPasswordRequest(user.getEmail());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class), anyLong())).thenReturn("token");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        doNothing().when(javaMailSender).send(mimeMessage);

        Template template = mock(Template.class);
        when(configuration.getTemplate(anyString())).thenReturn(template);
        doNothing().when(template).process(anyMap(), any(StringWriter.class));

        ForgotPasswordResponse response = passwordResetService.sendForgotPasswordEmail(request);

        Assertions.assertThat(response.getMessage()).isEqualTo("Email sent successfully...");
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void PasswordResetService_DeletePasswordResetsById_ReturnNothing() {
        doNothing().when(passwordResetRepository).deleteUserPasswordResetsById(user.getId());

        passwordResetService.deletePasswordResetsById(user.getId());

        verify(passwordResetRepository, times(1)).deleteUserPasswordResetsById(user.getId());
    }
}


