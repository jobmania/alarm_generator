package com.son.alarm_pjt.scheduler;

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
import java.util.stream.Collectors;

@Component
@Slf4j
public class PMSScheduler {

    private final List<String> memberList = new ArrayList<>(
            Arrays.asList(
                     "이승연"
                    ,"최현순"
                    ,"윤지연"
                    ,"김진규"
                    ,"김건우"
                    ,"최진휘"
                    ,"정민찬"
                    ,"조윤지"
                    ,"정근준"
                    ,"김예인"
                    ,"김규민"
                    ,"김진영"
                    ,"채민규"
                    ,"윤보민"
                    ,"배소정"
                    ,"손원철"
                    ,"신슬기"
            ));


    @Scheduled(cron = "0 0 14 ? * THUR")
    public void createCleanPlan(){

        List<String> exceptionMemberList = new ArrayList<>();

        /**
         * 1.  셀레니움으로 PMS 명단 확인하기 .
         * */
        try {
            List exceptionMembers = getExceptionMembers();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        /**
         * 2. 휴가자 제외 청소명단 생성..
         * */

        List<String> cleaningMembers =  memberList.stream()
                .filter( member -> !exceptionMemberList.contains(member))
                .toList();

        /**
         * 3. 몽고데이터 확인
         * */

    }


    private List getExceptionMembers() throws InterruptedException {
        String url = "http://pms.itnj.co.kr/ipms/loginPage.do";
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);

        // 'USER' 입력 필드에 값 입력
        WebElement userField = driver.findElement(By.name("USER"));
        userField.sendKeys("wcson");

        // 'PSWD' 입력 필드에 값 입력
        WebElement passwordField = driver.findElement(By.name("PSWD"));
        passwordField.sendKeys("11111");

        // 로그인 버튼 클릭 또는 로그인 함수 실행
        WebElement loginButton = driver.findElement(By.cssSelector("input.login_btn"));
        loginButton.click();

        // 로그인 완료 ! 및 대기
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        Thread.sleep(5000);


        // frame 이름으로 전환
        driver.switchTo().frame("right");


        // 연차목록을 확인한다
        WebElement tab1Div = driver.findElement(By.id("tab-1"));

        // Switching to the iframe inside the div element
        WebElement iframeElement = tab1Div.findElement(By.tagName("iframe"));
        driver.switchTo().frame(iframeElement);


        WebElement calendar = driver.findElement(By.id("calendar"));
        WebElement todayElement = calendar.findElement(By.className("today"));
        WebElement dots = todayElement.findElement(By.className("dots"));
        List<WebElement> liElements = dots.findElements(By.tagName("li"));

        // 연차리스트 저장.
        List<String> exceptionMemberList = new ArrayList<>();
        for (WebElement li : liElements) {
            String text = li.getText();
            exceptionMemberList.add(text);
        }


        // WebDriver 종료
        driver.quit();

        return exceptionMemberList;
    }
}
