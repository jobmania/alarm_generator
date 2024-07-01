package com.son.alarm_pjt.service;

import com.son.alarm_pjt.domain.Cleaning;
import com.son.alarm_pjt.domain.Enum.Gender;
import com.son.alarm_pjt.domain.Enum.Level;
import com.son.alarm_pjt.domain.Member;
import com.son.alarm_pjt.domain.ResponseDto;
import com.son.alarm_pjt.domain.Task;
import com.son.alarm_pjt.domain.response.ListDto;
import com.son.alarm_pjt.repository.CleaningRepository;
import com.son.alarm_pjt.repository.MemberRepository;
import com.son.alarm_pjt.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoService {
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final CleaningRepository cleaningRepository;
    private final RestTemplate restTemplate;
    private final String TeamsURL = "https://itnj1.webhook.office.com/webhookb2/647c0a1a-3c0b-4a00-a5e0-657658024ff3@1cbb409f-7344-429e-99c2-7d5266123c71/IncomingWebhook/a67ccab2ff1b4cfc8ef9037e0fd1942a/e769085a-09d4-468e-a90d-a2154c82b5ca";

    @Transactional
    public boolean insertTest(){
        try {

            /***
             * 특정일자 지우기 .
             * */
//           cleaningRepository.deleteCleaningByDate("2024-06-13");
//           cleaningRepository.deleteCleaningByDate("2024-05-23");

            /**
             * 멤버 추가하기 ..
             * */
//            Optional<Member> 김예진 = memberRepository.findByName("김예진");
//
//            if(김예진.isPresent()){
//                Member member = 김예진.get();
//                member.getId();
//            }


            /**
             * 추가하기
             * */

            /**
             * 지연 - 손걸레
             * 예진 - 밀대
             * 예인 - 밀대
             * 예인 - 커피머신
             * 민찬- 청소기
             * 진영 - 밀대
             * 윤지 - 쓰레기비우기
             * 민규 - 청소기
             * */

            List<Member> memberList = memberRepository.findAll();

            for (Member member : memberList) {

                switch (member.getName()) {
//                    case "윤지연":
//                        Task 손걸레 = taskRepository.findByName("손걸레");
//                        cleaningRepository.save(new Cleaning(member, 손걸레, "2024-06-13"));
//
//                        break;
//                    case "김예진":
//                        Task 밀대 = taskRepository.findByName("밀대");
//                        cleaningRepository.save(new Cleaning(member, 밀대, "2024-06-13"));
//                        break;
//
//                    case "김예인":
//                        Task 커피 = taskRepository.findByName("커피머신,냉장고");
//                        cleaningRepository.save(new Cleaning(member, 커피, "2024-06-13"));
//                        Task 밀대2 = taskRepository.findByName("밀대");
//                        cleaningRepository.save(new Cleaning(member, 밀대2, "2024-06-13"));
//                        break;
//
//                    case "정민찬":
//                        Task 청소기 = taskRepository.findByName("청소기");
//                        cleaningRepository.save(new Cleaning(member, 청소기, "2024-06-13"));
//                        break;

                    case "김진영":
                        Task 에어컨필터 = taskRepository.findByName("에어컨필터");
                        cleaningRepository.save(new Cleaning(member, 에어컨필터, "2024-06-20"));
                        break;

//                    case "조윤지":
//                        Task 쓰레기비우기 = taskRepository.findByName("쓰레기비우기");
//                        cleaningRepository.save(new Cleaning(member, 쓰레기비우기, "2024-06-13"));
//                        break;

                    case "김건우":
                        Task 에어컨필터2 = taskRepository.findByName("에어컨필터");
                        cleaningRepository.save(new Cleaning(member, 에어컨필터2, "2024-06-20"));
                        break;
                }
            }


//            List<Cleaning> allByDateAfter = cleaningRepository.findAllByDateAfter("2024-06-12");
//            for (Cleaning cleaning : allByDateAfter) {
//                System.out.println("cleaning.getMember().getName() = " + cleaning.getMember().getName());
//                System.out.println("cleaning.getTask().getName() = " + cleaning.getTask().getName());
//            }


        }catch (RuntimeException e){
            return false;
        }
        return true;
    }


    @Transactional
    public ResponseDto<?> insertCleaningData(){

        List<Cleaning> responseList = new ArrayList<>();
        List<ListDto> responseDtoList = new ArrayList<>();

        /**  1.멤버 조회하기.
         *     1) 셀레니움으로 PMS 연차자 명단 확인하기 .
         *     2) 청소참여 인원 리스트 DB에서 조회
         * */
        try {
            List<String> exceptionMembers = getExceptionMembers();
            List<Member> memberAllList = memberRepository.findAll();
            log.info("전체 멤버 ={}",memberAllList);

            String nowTimeString = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            // 현재 시간에서 4주 전의 시간 계산
            LocalDateTime fiveWeeksAgo = LocalDateTime.now().minusWeeks(4);
            LocalDateTime oneQuarterAgo = LocalDateTime.now().minusWeeks(12);
            // ISO 8601 형식으로 변환 => "2024-05-02"
            String fourWeeksAgoString = fiveWeeksAgo.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String oneQuarterAgoString = oneQuarterAgo.format(DateTimeFormatter.ISO_LOCAL_DATE);

            // 4주전 청소작업리스트 출력.
            List<Cleaning> pastCleaningList = cleaningRepository.findAllByDateAfter(fourWeeksAgoString);

            /**
             * 2. 인원 제외 및 정렬
             *    1) 연차 인원 제외
             *    2) 청소 횟수 덜한 인원 우선 배치.
             * */

            // 각 멤버별 포함된 횟수 계산
            Map<Member, Long> memberCounts = pastCleaningList.stream()
                    .map(Cleaning::getMember)
                    .collect(Collectors.groupingBy(m -> m, Collectors.counting()));

            // 포함된 횟수가 적은 순으로 정렬
            List<Member> sortedMembers = memberCounts.entrySet().stream()
                    .sorted(Comparator.comparingLong(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            // 없는 멤버를 멤버 리스트의 맨 앞에 추가
            List<Member> resultMembers = new ArrayList<>();
            resultMembers.addAll(memberAllList.stream()
                    .filter(member -> !sortedMembers.contains(member))
                    .collect(Collectors.toList()));
            resultMembers.addAll(sortedMembers);

            // 연차인원 제외하기
            List<Member> memberList = resultMembers.stream()
                    .filter(m -> !exceptionMembers.contains(m.getName()))
                    .collect(Collectors.toList());

            /**
             * 3. 청소 목록 생성하기
             *   - 한달에 한번하는 청소 목록 : 화분, 공기청정기, 에어컨필터
             *   - 청소 배정 갯수만큼 생성되도록.
             * */

            List<Task> taskPerList = new LinkedList<>();
            List<Task> taskList = taskRepository.findAll();
            for (Task task : taskList) {

                // 업무마다 배정수가 정해져있음.
                for (int i = 0; i < task.getAssignNum(); i++) {

                    // 1. 화분, 2. 공기청정기
                    if(task.getName().equals("화분") || task.getName().equals("공기청정기")) {
                        List<Cleaning> monthList = cleaningRepository.findAllByTaskNameInAndDateAfter(Arrays.asList("화분", "공기청정기"), fourWeeksAgoString);
                        if (monthList.isEmpty()) {
                            taskPerList.add(task);
                        }

                    // 3.  에어컨 필터
                    } else if ( task.getName().equals("에어컨필터") ) {
                        List<Cleaning> monthList2 = cleaningRepository.findAllByTaskNameInAndDateAfter(Arrays.asList("에어컨필터"), oneQuarterAgoString);
                        if (monthList2.isEmpty()) {
                            taskPerList.add(task);
                        }
                  // 나머지는 매주 청소.
                    } else {
                        taskPerList.add(task);
                    }
                }
            }

            /**
             * 4 : 인원 청소 배정
             *  - 중복 없이
             *  - gender에 맞게
             *  - 청소 미참여 횟수 순으로 정렬.
             * */

            // 랜덤으로 청소업무 섞어 버리기
            Collections.shuffle(taskPerList);

            Queue<Task> taskQueue = new LinkedList<>();

            // 리스트의 모든 요소를 큐에 추가
            for (Task task : taskPerList) {
                taskQueue.offer(task); // 큐에 요소 추가
            }

            // 배정할 Task보다 참여할 인원이 적다면 예외케이스 발생.
            if(taskQueue.size() > memberList.size() ){
                sendTeamTopic(responseDtoList,nowTimeString);
                throw new RuntimeException("흑흑 청소할 사람이 없어요 : [ , \n 자리정돈 및 쓰레기만 비웁시다. " );
            }


            while (!taskQueue.isEmpty()){
                Task task = taskQueue.poll();
                boolean isAssigned = false;

                for (Member member : memberList){

                    // 5주간 해당업무에 배정된 내역 확인
                    Optional<Member> assignMember = cleaningRepository.findByTaskNameAndMemberNameAndDateAfter(task.getName(), member.getName(), fourWeeksAgoString);


                    if(assignMember.isEmpty()){
                        // 특정업무는 Gender에 갈린다.
                        if (!task.getGender().equals(Gender.중성)) {
                            if ( task.getGender().equals(member.getGender())){
                                responseList.add(new Cleaning(member,task,nowTimeString));
                                memberList.remove(member);
                                isAssigned = true;
                                break ;
                            }
                        }else {
                            responseList.add(new Cleaning(member,task,nowTimeString));
                            memberList.remove(member);
                            isAssigned = true;
                            break ;
                        }
                    }else {
                        log.info("중복 업무 이름 {}, {}",assignMember.get().getName(),task.getName());
                    }
                }

                if( !isAssigned){
                    // 배정받지 못한 업무는 랜덤으로 배정.

                    while(!isAssigned){
                        // 랜덤 객체 생성
                        Random random = new Random();
                        // 리스트의 크기
                        int listSize = memberList.size();
                        // 랜덤 인덱스 생성
                        int randomIndex = random.nextInt(listSize);
                        Member member = memberList.get(randomIndex);

                        if(member.getGender().equals(task.getGender())){
                            responseList.add(new Cleaning(member,task,nowTimeString));
                            memberList.remove(member);
                            isAssigned = true;
                        }
                    }
                }
            }


            /**
             * 5. 생성된 리스트 db에 저장.
             * */

            cleaningRepository.saveAll(responseList);

            /**
             * 6 : 생성된 리스트  Teams로 쏴주기
             * */
            for (Cleaning cleaning : responseList) {
                responseDtoList.add(new ListDto(cleaning.getMember().getName(), cleaning.getTask().getName(), cleaning.getDate()));
            }


            responseDtoList = responseDtoList.stream()
                    .sorted(Comparator.comparing(ListDto::getTaskName))
                    .collect(Collectors.toList());

            sendTeamTopic(responseDtoList, nowTimeString);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        return ResponseDto.success(responseDtoList,"성공");
    }

    public List<ListDto> getTasks(String date) {
        List<ListDto> responseDtoList = new ArrayList<>();
        List<Cleaning> allByDate = cleaningRepository.findAllByDate(date);

        for (Cleaning cleaning : allByDate) {
            responseDtoList.add(new ListDto(cleaning.getMember().getName(), cleaning.getTask().getName(), cleaning.getDate()));
        }

        return responseDtoList;
    }

    public boolean sendTeamTopic(List<ListDto> dataList, String date){

        String title = date + "일자 청소 목록입니다 : ] ";
        String text = "";
        if(dataList.isEmpty()){
            text = "흑흑 청소할 사람이 없어요  : [";
        }else {


            StringBuilder sb = new StringBuilder();
            sb.append("     일자     |     이름     |     역할     \n");
            sb.append("--------------------------------------------------  \n");
            sb.append(" \n");
            for (ListDto listDto : dataList) {
                sb.append(String.format("     %s     |     %s     |     %s     \n", date, listDto.getMemberName(), listDto.getTaskName()));
            }

            text = sb.toString();

        }


        // Request Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Request Body 설정 및 직렬화.
        JSONObject requestBody = new JSONObject();

        requestBody.put("title", title);
        requestBody.put("text", text);
        requestBody.put("@context", "https://schema.org/extensions");
        requestBody.put("@type", "MessageCard");

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(),headers);

        // post 요청
        String exchange = restTemplate.postForObject(TeamsURL, request, String.class);
        log.info("알람응답값 = {} ",exchange);

        return true;
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

    @Transactional
    public boolean tempLogic(){
        try {
            // 05-02 데이터 수기로 넣기;

//           cleaningRepository.deleteCleaningByDate("2024-05-23");

//            Optional<Member> 김예진 = memberRepository.findByName("김예진");
//
//            if(김예진.isPresent()){
//                Member member = 김예진.get();
//                member.getId();
//            }


            List<String> exceptionMembers = getExceptionMembers();
            List<Member> memberAllList = memberRepository.findAll();
            List<Member> memberList = memberAllList.stream()
                    .filter(m -> !exceptionMembers.contains(m.getName()))
                    .collect(Collectors.toList());


            Collections.shuffle(memberList);

            for (Member member : memberList) {
                if(member.getGender().equals(Gender.여성)){
                    System.out.println("여성화장실 당첨 = "+ member.getName());
                    memberList.remove(member);
                    break;
                }
            }


            Collections.shuffle(memberList);

            for (int i = 0; i < 2; i++) {
                System.out.println("일반쓰레기 비우기 = " + memberList.get(i).getName());
            }




        }catch (RuntimeException e){
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Transactional
    public void deleteMember(String name) {
        int memberDelete = memberRepository.deleteByName(name);
        int cleaningDelete = cleaningRepository.deleteByMemberName(name);

        log.info("memberDelete ={},",memberDelete);
        log.info("cleaningDelete ={},",cleaningDelete);
    }

    @Transactional
    public Cleaning insertCleaning(String date, String memberName, String taskName ) {

        Member findMember = memberRepository.findByName(memberName);
        Task findTask = taskRepository.findByName(taskName);

        Cleaning save = cleaningRepository.save(new Cleaning(findMember, findTask, date));

        return save;
    }
}
