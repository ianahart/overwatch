package com.hart.overwatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
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
    public RedisTemplate<String, EmailRequest> emailRedisTemplate() {
        RedisTemplate<String, EmailRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
