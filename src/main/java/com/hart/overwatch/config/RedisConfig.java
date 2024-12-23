package com.hart.overwatch.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.hart.overwatch.chatmessage.ChatMessageSubscriber;
import com.hart.overwatch.email.request.EmailRequest;
import com.hart.overwatch.teammessage.TeamMessageSubscriber;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${REDIS_URL}")
    private String redisUrl;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() throws URISyntaxException {
        if (redisUrl == null || redisUrl.isEmpty()) {
            RedisStandaloneConfiguration localRedisConfig = new RedisStandaloneConfiguration();
            localRedisConfig.setHostName("localhost");
            localRedisConfig.setPort(6379);
            return new LettuceConnectionFactory(localRedisConfig);
        }

        URI uri = new URI(redisUrl);
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(uri.getHost());
        redisConfig.setPort(uri.getPort());

        if (uri.getUserInfo() != null && uri.getUserInfo().contains(":")) {
            String password = uri.getUserInfo().split(":")[1];
            redisConfig.setPassword(password);
        }

        LettuceClientConfiguration clientConfig =
                LettuceClientConfiguration.builder().useSsl().disablePeerVerification()

                        .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws URISyntaxException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
            MessageListenerAdapter chatListenerAdapter,
            MessageListenerAdapter teamListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(chatListenerAdapter, new PatternTopic("chat"));
        container.addMessageListener(teamListenerAdapter, new PatternTopic("team"));

        return container;
    }

    @Bean
    MessageListenerAdapter chatListenerAdapter(ChatMessageSubscriber receiver) {
        return new MessageListenerAdapter(receiver, "receiveChatMessage");
    }

    @Bean
    MessageListenerAdapter teamListenerAdapter(TeamMessageSubscriber receiver) {
        return new MessageListenerAdapter(receiver, "receiveTeamMessage");
    }

    @Bean
    public RedisTemplate<String, EmailRequest> emailRedisTemplate() throws URISyntaxException {
        RedisTemplate<String, EmailRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
