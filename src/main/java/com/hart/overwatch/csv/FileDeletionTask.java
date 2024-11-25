package com.hart.overwatch.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileDeletionTask {

    @Autowired
    private CsvFileService csvFileService;

    @Scheduled(fixedDelay = 30000)
    public void processDeletions() {
        csvFileService.processFileDeletions();
    }
}
