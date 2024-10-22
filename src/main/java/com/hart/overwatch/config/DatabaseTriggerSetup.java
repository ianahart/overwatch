package com.hart.overwatch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;


@Component
public class DatabaseTriggerSetup {

    private final DatabaseSetupService databaseSetupService;

    @Autowired
    public DatabaseTriggerSetup(DatabaseSetupService databaseSetupService) {
        this.databaseSetupService = databaseSetupService;
    }

    @PostConstruct
    public void init() {
        databaseSetupService.createTsvectorTrigger();
    }
}
