package com.hart.overwatch.passwordreset;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.passwordreset.request.ForgotPasswordRequest;
import com.hart.overwatch.passwordreset.request.PasswordResetRequest;
import com.hart.overwatch.passwordreset.response.ForgotPasswordResponse;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.util.MyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class PasswordResetService {

    @Value("${secretkey}")
    private String secretKey;

    @Value("${DEFAULT_TTL}")
    private Long DEFAULT_TTL;

    private final JwtService jwtService;
    private final Configuration configuration;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(JwtService jwtService, Configuration configuration,
            JavaMailSender javaMailSender, UserRepository userRepository,
            PasswordResetRepository passwordResetRepository, UserService userService,
            PasswordEncoder passwordEncoder) {

        this.jwtService = jwtService;
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${emailsender}")
    private String sender;

    public void deleteUserPasswordResetsById(Long id) {
        this.passwordResetRepository.deleteUserPasswordResetsById(id);
    }

    private void savePasswordReset(User user, String code, String token) {
        this.passwordResetRepository.save(new PasswordReset(user, code, token));
    }

    public ForgotPasswordResponse sendForgotPasswordEmail(ForgotPasswordRequest request)
            throws MessagingException, IOException, TemplateException {

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(sender);
        helper.setSubject("Reset Your Password");

        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("A user with this email does not exist."));

        helper.setTo(request.getEmail());
        String emailContent = getEmailContent(user);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);

        return new ForgotPasswordResponse("Email sent successfully...");
    }

    String getEmailContent(User user) throws IOException, TemplateException {
        String token = this.jwtService.generateToken(user, DEFAULT_TTL);
        int min = 10000;
        int max = 20000;
        String code = String.valueOf(new Random().nextInt((max - min) + 1) + min);
        savePasswordReset(user, code, token);

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("token", token);
        model.put("code", code);
        this.configuration.getTemplate("forgot-password-email.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    private PasswordReset getPasswordReset(String token) {
        Claims claims = extractUserIdFromToken(token);
        User user = this.userService.getUserByEmail(claims.getSubject());
        return this.passwordResetRepository.getPasswordResetByUserId(user.getId());

    }


    public boolean checkPasswordResetExpired(String token) {
        return this.jwtService.tokenElapsedDay(token);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token)
                .getBody();

    }

    private boolean checkIfTokenExpired(String token) {
        return this.jwtService.tokenElapsedDay(token);
    }

    public void deletePasswordResetsById(Long id) {
        this.passwordResetRepository.deleteUserPasswordResetsById(id);
    }

    private boolean passwordResetExists(Long userId, String token) {
        return this.passwordResetRepository.findPasswordResetByUserIdAndToken(userId, token);
    }

    public void resetPassword(PasswordResetRequest request) {
        Claims claims = extractUserIdFromToken(request.getToken());

        User user = this.userService.getUserByEmail(claims.getSubject());

        if (checkIfTokenExpired(request.getToken())
                && passwordResetExists(user.getId(), request.getToken())) {
            throw new BadRequestException(
                    "Token has expired. Please try resetting your password again");
        }

        if (!MyUtil.validatePassword(request.getPassword())) {
            throw new BadRequestException(
                    "Password must include 1 uppercase, 1 lowercase, 1 digit and 1 special char");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        user.setPassword(this.passwordEncoder.encode(request.getPassword()));

        deletePasswordResetsById(user.getId());
    }
}

