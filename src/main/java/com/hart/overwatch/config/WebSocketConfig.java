package com.hart.overwatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${app.production.url}")
    private String productionUrl;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        if (productionUrl.equals("https://codeoverwatch.com")) {
            registry.addEndpoint("/wss").setAllowedOrigins("https://codeoverwatch.com")
                    .withSockJS();
        } else {
            registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5173/").withSockJS();
        }
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/api/v1");
        registry.enableSimpleBroker("/user");
        registry.setUserDestinationPrefix("/user");
    }
}
