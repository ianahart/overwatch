package com.hart.overwatch.authentication;

import java.io.IOException;

import com.hart.overwatch.authentication.request.LoginRequest;
import com.hart.overwatch.authentication.request.RegisterRequest;
import com.hart.overwatch.authentication.request.VerifyOTPRequest;
import com.hart.overwatch.authentication.response.GetOtpResponse;
import com.hart.overwatch.authentication.response.LoginResponse;
import com.hart.overwatch.authentication.response.RegisterResponse;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.passwordreset.PasswordResetService;
import com.hart.overwatch.passwordreset.request.ForgotPasswordRequest;
import com.hart.overwatch.passwordreset.request.PasswordResetRequest;
import com.hart.overwatch.passwordreset.response.ForgotPasswordResponse;
import com.hart.overwatch.passwordreset.response.PasswordResetResponse;
import com.hart.overwatch.refreshtoken.RefreshToken;
import com.hart.overwatch.refreshtoken.RefreshTokenService;
import com.hart.overwatch.refreshtoken.request.RefreshTokenRequest;
import com.hart.overwatch.refreshtoken.response.RefreshTokenResponse;
import com.hart.overwatch.token.TokenService;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    @Value("${DEFAULT_TTL}")
    private Long DEFAULT_TTL;

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
            RefreshTokenService refreshTokenService, JwtService jwtService,
            TokenService tokenService, UserService userService,
            PasswordResetService passwordResetService) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        this.authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("success"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.authenticationService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken =
                this.refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        this.tokenService.revokeAllUserTokens(refreshToken.getUser());
        String token = this.jwtService.generateToken(refreshToken.getUser(), DEFAULT_TTL);
        this.authenticationService.saveTokenWithUser(token, refreshToken.getUser());

        return ResponseEntity.status(200)
                .body(new RefreshTokenResponse(token, refreshToken.getRefreshToken()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> sendEmail(
            @RequestBody ForgotPasswordRequest request)
            throws IOException, TemplateException, MessagingException {
        User user = this.userService.getUserByEmail(request.getEmail());

        this.passwordResetService.deleteUserPasswordResetsById(user.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.passwordResetService.sendForgotPasswordEmail(request));

    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(
            @RequestBody PasswordResetRequest request) {
        this.passwordResetService.resetPassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(new PasswordResetResponse("success"));
    }

    @GetMapping(path = "/generate-otp")
    public ResponseEntity<GetOtpResponse> getOtp(@RequestParam("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK)

                .body(new GetOtpResponse("success",

                        this.authenticationService.generateOTP(userId)));
    }

    @PostMapping(path = "/verify-otp")
    public ResponseEntity<LoginResponse> verifyOtp(@RequestBody VerifyOTPRequest request)
            throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.authenticationService.verifyOTP(request.getUserId(), request.getOtpCode()));
    }

}

