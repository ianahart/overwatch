package com.hart.overwatch.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.hart.overwatch.email.request.EmailRequest;

@Service
public class EmailQueueService {

    private static final String EMAIL_QUEUE = "emailQueue";

    private final RedisTemplate<String, EmailRequest> redisTemplate;

    @Autowired
    public EmailQueueService(RedisTemplate<String, EmailRequest> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void queueEmail(EmailRequest emailRequest) {
        redisTemplate.opsForList().leftPush(EMAIL_QUEUE, emailRequest);
    }
}
