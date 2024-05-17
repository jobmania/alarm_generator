package com.son.alarm_pjt.scheduler;

import com.son.alarm_pjt.service.MongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PMSScheduler {
    private final MongoService mongoService;


    @Scheduled(cron = "0 09 05 ? * THU")
    public void createCleanPlan(){
        mongoService.insertCleaningData();
    }

}
