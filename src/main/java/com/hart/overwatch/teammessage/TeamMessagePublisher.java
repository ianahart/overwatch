package com.hart.overwatch.teammessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TeamMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void sendMessage(String teamMessage) {
        redisTemplate.convertAndSend("team", teamMessage);
    }
}


