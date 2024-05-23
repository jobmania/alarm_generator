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


    //초(0-59), 분(0-59), 시간(0-23), 일(1-31), 월(1-12), 요일(0-6) (0: 일, 1: 월, 2:화, 3:수, 4:목, 5:금, 6:토)
    @Scheduled(cron = "0 10 9 ? * THU")
    public void createCleanPlan(){
        mongoService.insertCleaningData();
    }

}
