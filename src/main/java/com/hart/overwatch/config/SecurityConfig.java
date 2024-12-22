package com.hart.overwatch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.production.url}")
    private String productionUrl;

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;
    private final RateLimitingFilter rateLimitingFilter;

    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter, LogoutHandler logoutHandler,
            RateLimitingFilter rateLimitingFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutHandler = logoutHandler;
        this.rateLimitingFilter = rateLimitingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            String localWebSocketUrl = "http://localhost:8080/ws";
            String productionWebSocketUrl = "wss://codeoverwatch.com/ws";
            String backendApiUrl = "https://hart-codeoverwatch-ac78ceac3e31.herokuapp.com";
            String allowedOrigin =
                    (productionUrl != null && productionUrl.equals("https://codeoverwatch.com"))
                            ? "https://codeoverwatch.com"
                            : "http://localhost:5173";

            config.setAllowedOrigins(

                    List.of(allowedOrigin, localWebSocketUrl, productionWebSocketUrl,
                            backendApiUrl));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
            config.setAllowedHeaders(
                    List.of("Authorization", "Cache-Control", "Content-Type", "GitHub-Token"));
            config.setAllowCredentials(true);
            return config;
        })).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/").permitAll()
                        .requestMatchers("/api/v1/auth/**", "ws/**", "wss/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/topics/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/app-testimonials/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/topics/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN").anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(this.authenticationProvider)
                .addFilterBefore(this.rateLimitingFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler).logoutSuccessHandler((request, response,
                                authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }
}

