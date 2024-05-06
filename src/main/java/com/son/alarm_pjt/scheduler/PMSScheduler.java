package com.son.alarm_pjt.scheduler;

import com.son.alarm_pjt.repository.MemberRepository;
import com.son.alarm_pjt.service.MongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PMSScheduler {
    private final MongoService mongoService;


    @Scheduled(cron = "0 30 14 ? * THU")
    public void createCleanPlan(){





    }

}
