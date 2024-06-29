package com.hart.overwatch.chatmessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void sendMessage(String chatMessage) {
        redisTemplate.convertAndSend("chat", chatMessage);
    }
}

