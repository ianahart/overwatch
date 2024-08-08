package com.hart.overwatch.user;

import java.security.Key;
import java.util.Optional;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.token.TokenService;
import com.hart.overwatch.user.dto.UpdateUserDto;
import com.hart.overwatch.user.dto.UserDto;
import com.hart.overwatch.user.request.UpdateUserRequest;
import com.hart.overwatch.util.MyUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.ConstraintViolationException;

@Service
public class UserService {

    @Value("${secretkey}")
    private String secretKey;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public boolean userExistsByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent();
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with that email does not exist"));
    }

    public User getUserById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                String.format("A user with the id %d does not exist", userId)));
    }

    public User getCurrentlyLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetails) principal).getUsername();
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Current user was not found"));
        return user;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token)
                .getBody();

    }

    public UserDto getUserByToken(String token) {
        Claims claims = extractUserIdFromToken(token);

        User user = getUserByEmail(claims.getSubject());
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getRole(), user.getAbbreviation(), user.getLoggedIn(),
                user.getProfile().getId(), user.getProfile().getAvatarUrl(), user.getFullName(),
                user.getSetting().getId(), user.getSlug());

    }

    private boolean verifyPassword(String currentPassword, String hashedPassword,
            String newPassword) {

        try {
            if (currentPassword == null || hashedPassword == null || newPassword == null) {
                return false;
            }

            if (this.passwordEncoder.matches(newPassword, hashedPassword)) {
                return false;
            }
            return this.passwordEncoder.matches(currentPassword, hashedPassword);

        } catch (IllegalArgumentException ex) {
            return false;
        }

    }

    public void updateUserPassword(String currentPassword, String newPassword, Long userId) {
        try {

            User user = getCurrentlyLoggedInUser();

            if (user.getId() != userId) {
                throw new ForbiddenException("Cannot update another user's password");
            }

            if (!verifyPassword(currentPassword, user.getPassword(), newPassword)) {
                throw new ForbiddenException(
                        "Your current password is invalid or is the same as your old one");
            }

            if (!MyUtil.validatePassword(newPassword)) {
                throw new BadRequestException(
                        "Password must include 1 uppercase 1 lowercase 1 digit and 1 special character");
            }

            user.setPassword(this.passwordEncoder.encode(newPassword));

            this.userRepository.save(user);

        } catch (DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteUser(Long userId, String password) {
        try {

            User user = this.getCurrentlyLoggedInUser();

            if (user.getId() != userId) {
                throw new ForbiddenException("Cannot delete another user");
            }

            if (!this.passwordEncoder.matches(password, user.getPassword())) {
                throw new ForbiddenException("Password is not a match");
            }

            this.userRepository.delete(user);
        } catch (ForbiddenException ex) {
            throw new ForbiddenException(ex.getMessage());
        }
    }

    public UpdateUserDto updateUser(UpdateUserRequest request, Long userId) {
        try {
            User user = getCurrentlyLoggedInUser();

            if (user.getId() != userId) {
                throw new ForbiddenException("Cannot update another user's information");
            }

            user.setFirstName(
                    Jsoup.clean(MyUtil.capitalize(request.getFirstName()), Safelist.none()));
            user.setLastName(Jsoup.clean(MyUtil.capitalize(request.getLastName()), Safelist.none()));
            user.setEmail(Jsoup.clean(request.getEmail(), Safelist.none()));

            this.userRepository.save(user);

            return new UpdateUserDto(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getAbbreviation());

        } catch (ConstraintViolationException ex) {
            return null;

        }
    }
}
