package com.son.alarm_pjt.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CrawlingService {
    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
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
        List<String> textList = new ArrayList<>();
        for (WebElement li : liElements) {
            String text = li.getText();
            textList.add(text);
            System.out.println("text = " + text);
        }


        // WebDriver 종료
        driver.quit();
    }


}
