package com.hart.overwatch.config;

import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.refreshtoken.RefreshTokenService;
import com.hart.overwatch.token.TokenRepository;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RefreshTokenService refreshTokenService;

    public LogoutService(JwtService jwtService, UserRepository userRepository,
            TokenRepository tokenRepository, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);

        var storedToken = this.tokenRepository.findByToken(jwt).orElse(null);
        User user = this.userRepository.findByEmail(this.jwtService.extractUsername(jwt))
                .orElseThrow(() -> new NotFoundException("User not found logging out."));

        this.userRepository.updateLoggedIn(user.getId(), false);
        storedToken.setRevoked(true);
        storedToken.setExpired(true);
        this.tokenRepository.save(storedToken);
        this.refreshTokenService.revokeAllUserRefreshTokens(user);
        SecurityContextHolder.clearContext();

    }
}

