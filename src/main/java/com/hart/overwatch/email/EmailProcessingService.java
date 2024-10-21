package com.hart.overwatch.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.hart.overwatch.email.request.EmailRequest;

@Service
public class EmailProcessingService {

    private static final String EMAIL_QUEUE = "emailQueue";

    private final RedisTemplate<String, EmailRequest> redisTemplate;

    private final EmailSender emailSender;

    @Autowired
    public EmailProcessingService(RedisTemplate<String, EmailRequest> redisTemplate,
            EmailSender emailSender) {
        this.redisTemplate = redisTemplate;
        this.emailSender = emailSender;
    }

    @Scheduled(fixedDelay = 5000)
    public void processQueuedEmails() {
        EmailRequest emailRequest;
        while ((emailRequest = redisTemplate.opsForList().rightPop(EMAIL_QUEUE)) != null) {
            emailSender.sendEmail(emailRequest);
        }
    }
}
