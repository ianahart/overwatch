package com.hart.overwatch.authentication;

import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.authentication.dto.AuthDto;
import com.hart.overwatch.authentication.request.LoginRequest;
import com.hart.overwatch.authentication.request.RegisterRequest;
import com.hart.overwatch.authentication.response.LoginResponse;
import com.hart.overwatch.authentication.response.RegisterResponse;
import com.hart.overwatch.ban.BanService;
import com.hart.overwatch.config.JwtService;
import com.hart.overwatch.phone.PhoneService;
import com.hart.overwatch.profile.ProfileService;
import com.hart.overwatch.refreshtoken.RefreshToken;
import com.hart.overwatch.refreshtoken.RefreshTokenService;
import com.hart.overwatch.setting.SettingService;
import com.hart.overwatch.token.Token;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.token.TokenService;
import com.hart.overwatch.token.TokenType;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.user.dto.UserDto;
import com.hart.overwatch.util.MyUtil;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@Service
public class AuthenticationService {

    @Value("${DEFAULT_TTL}")
    private Long DEFAULT_TTL;

    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final UserService userService;
    private final SettingService settingService;
    private final PhoneService phoneService;
    private final BanService banService;

    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder, ProfileService profileService,
            UserRepository userRepository, AuthenticationManager authenticationManager,
            JwtService jwtService, TokenRepository tokenRepository,
            RefreshTokenService refreshTokenService, TokenService tokenService,
            UserService userService, SettingService settingService, PhoneService phoneService,
            BanService banService) {
        this.passwordEncoder = passwordEncoder;
        this.profileService = profileService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.settingService = settingService;
        this.phoneService = phoneService;
        this.banService = banService;
    }


    public LoginResponse verifyOTP(Long userId, String otpCode) throws Exception {
        try {
            User user = this.userService.getUserById(userId);
            boolean isVerified = this.phoneService.verifyUserOTP(user, otpCode);
            AuthDto authenticationItems = postAuthenticationSteps(user);

            if (isVerified) {
                return new LoginResponse(authenticationItems.getUser(),
                        authenticationItems.getJwtToken(),
                        authenticationItems.getRefreshToken().getRefreshToken());

            } else {
                throw new BadRequestException("Incorrect pass code");
            }



        } catch (Exception ex) {
            throw new BadRequestException("Incorrect pass code");
        }
    }


    public String generateOTP(Long userId) {
        return this.phoneService.generateUserOTP(userId);
    }

    private void validateRegistration(RegisterRequest request) {
        if (this.userService.userExistsByEmail(request.getEmail())) {
            throw new BadRequestException("A user with this email already exists");
        }

        if (!MyUtil.validatePassword(request.getPassword())) {

            throw new BadRequestException(
                    "Password must contain 1 letter, 1 number, 1 special char, and 1 uppercase letter");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
    }

    private Role setUserRole(String role) {

        return Role.valueOf(role) == Role.USER ? Role.USER : Role.REVIEWER;
    }

    public RegisterResponse register(RegisterRequest request) {
        validateRegistration(request);

        Role role = setUserRole(request.getRole());

        if (this.userRepository.countUsers() == 0) {
            role = Role.ADMIN;
        }

        String firstName = MyUtil.capitalize(request.getFirstName());
        String lastName = MyUtil.capitalize(request.getLastName());
        User user = new User(Jsoup.clean(request.getEmail(), Safelist.none()),
                Jsoup.clean(firstName, Safelist.none()), Jsoup.clean(lastName, Safelist.none()),
                String.format("%s %s", firstName, lastName), role, false,
                this.profileService.createProfile(),
                this.passwordEncoder.encode(request.getPassword()),
                this.settingService.createSetting());
        this.userRepository.save(user);
        return new RegisterResponse("User created");
    }

    public void saveTokenWithUser(String token, User user) {
        Token tokenToSave = new Token(token, TokenType.BEARER, false, false, user);
        this.tokenRepository.save(tokenToSave);

    }

    private UserDto updateAuthUser(User user, String jwtToken) {

        user.setLoggedIn(true);

        this.userRepository.save(user);
        this.saveTokenWithUser(jwtToken, user);

        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getRole(), user.getAbbreviation(), user.getLoggedIn(),
                user.getProfile().getId(), user.getProfile().getAvatarUrl(), user.getFullName(),
                user.getSetting().getId(), user.getSlug());

    }


    private AuthDto postAuthenticationSteps(User user) {
        String jwtToken = this.jwtService.generateToken(user, DEFAULT_TTL);
        this.tokenService.revokeAllUserTokens(user);
        UserDto userDto = this.updateAuthUser(user, jwtToken);
        RefreshToken refreshToken = this.refreshTokenService.generateRefreshToken(user.getId());

        return new AuthDto(userDto, jwtToken, refreshToken);

    }


    private Boolean is2FAEnabled(User user) {
        Boolean isEnabled = user.getSetting().getMfaEnabled();

        if (isEnabled != null) {
            if (isEnabled) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }


    private Boolean userHasPhone(User user) {
        return user.getPhones().getFirst().getPhoneNumber() != null;
    }

    public LoginResponse login(LoginRequest request) {

        try {


            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    Jsoup.clean(request.getEmail(), Safelist.none()),
                    Jsoup.clean(request.getPassword(), Safelist.none())));

        } catch (BadCredentialsException e) {
            throw new ForbiddenException("Credentials are invalid");
        }
        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found by email."));


        checkForBan(user);

        if (is2FAEnabled(user) && userHasPhone(user)) {
            return new LoginResponse(user.getId());
        }


        AuthDto authenticationItems = postAuthenticationSteps(user);

        return new LoginResponse(authenticationItems.getUser(), authenticationItems.getJwtToken(),
                authenticationItems.getRefreshToken().getRefreshToken());
    }


    private void checkForBan(User user) {
        if (user.getBan() != null) {
            if (user.getBan().getBanDate().isAfter(LocalDateTime.now())) {
                throw new ForbiddenException(
                        "Your account has been banned. Contact support at codeoverwatch@codeoverwatch.com");
            } else {
                banService.deleteBan(user.getBan().getId());
            }
        }
    }

}

