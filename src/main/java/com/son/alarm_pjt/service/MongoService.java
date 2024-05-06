package com.son.alarm_pjt.service;

import com.son.alarm_pjt.domain.Cleaning;
import com.son.alarm_pjt.domain.Enum.Gender;
import com.son.alarm_pjt.domain.Enum.Level;
import com.son.alarm_pjt.domain.Member;
import com.son.alarm_pjt.domain.ResponseDto;
import com.son.alarm_pjt.domain.Task;
import com.son.alarm_pjt.repository.CleaningRepository;
import com.son.alarm_pjt.repository.MemberRepository;
import com.son.alarm_pjt.repository.TaskRepository;
import com.son.alarm_pjt.scheduler.PMSScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoService {
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final CleaningRepository cleaningRepository;

    @Transactional
    public boolean insertTest(){
        try {
            // 05-02 데이터 수기로 넣기;

            cleaningRepository.save(new Cleaning(memberRepository.findByName("정민찬"), taskRepository.findByName("청소기"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("채민규"), taskRepository.findByName("청소기"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("최진휘"), taskRepository.findByName("유리창"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("김예인"), taskRepository.findByName("유리창"), "2024-05-02"));


            cleaningRepository.save(new Cleaning(memberRepository.findByName("손원철"), taskRepository.findByName("제빙기"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("윤지연"), taskRepository.findByName("손걸레"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("김규민"), taskRepository.findByName("공기청정기"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("최현순"), taskRepository.findByName("화분"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("배소정"), taskRepository.findByName("여자화장실"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("이승연"), taskRepository.findByName("여자화장실"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("윤보민"), taskRepository.findByName("밀대"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("정근준"), taskRepository.findByName("밀대"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("김진영"), taskRepository.findByName("밀대"), "2024-05-02"));
            cleaningRepository.save(new Cleaning(memberRepository.findByName("김진규"), taskRepository.findByName("커피머신,냉장고"), "2024-05-02"));



        }catch (RuntimeException e){
            return false;
        }
        return true;
    }


    @Transactional
    public ResponseDto<?> getTest(){

        /**
         * 1.  셀레니움으로 PMS 명단 확인하기 .
         * */
        try {
            List<String> exceptionMembers = getExceptionMembers();

            List<Member> memberList = memberRepository.findAll();

        /**
         * 2. 휴가자 제외 청소명단 생성..
         * */

            memberList = memberList.stream()
                    .filter(m -> !exceptionMembers.contains(m.getName()))
                    .toList();

        /**
         * 3. 몽고데이터 확인 => 이전 청소 명단 ( 5주전까지 확인!)
         * */

            // 현재 시간에서 5주 전의 시간 계산
            LocalDateTime fiveWeeksAgo = LocalDateTime.now().minusWeeks(5);
            // ISO 8601 형식으로 변환 => "2024-05-02"
            String fiveWeeksAgoString = fiveWeeksAgo.format(DateTimeFormatter.ISO_LOCAL_DATE);
            List<Cleaning> cleaningList = cleaningRepository.findAllByDateAfter(fiveWeeksAgoString);


        /**
         * 4. 청소배정하기 ( 5주 내용은 중복되지 않도록! )
         * */
            List<Task> taskList = taskRepository.findAll();

            for (Task task : taskList) {
                int num = task.getAssignNum();
                while(num!=0){



                    num--;
                }
            }


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ResponseDto.success("엄","섻");
    }


    public List<String> getExceptionMembers() throws InterruptedException {
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
