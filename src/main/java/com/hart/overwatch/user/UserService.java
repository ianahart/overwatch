package com.hart.overwatch.user;

import java.security.Key;
import java.util.Optional;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.token.TokenService;
import com.hart.overwatch.user.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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



}

