package com.hart.overwatch.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanUpJob {

    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(TokenCleanUpJob.class);

    @Autowired
    public TokenCleanUpJob(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTokenCleanUp() {
        logger.info("Running token cleanup job");
        this.tokenService.deleteAllExpiredTokens();
        logger.info("Finishing token cleanup job");
    }

}
