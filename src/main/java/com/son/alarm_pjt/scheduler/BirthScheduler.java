package com.son.alarm_pjt.scheduler;

import com.github.usingsky.calendar.KoreanLunarCalendar;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class BirthScheduler {
    private final RestTemplate restTemplate;
    private final static HashMap<String, LocalDate> birthMap = new HashMap<>();
    private final static HashMap<String, LocalDate> realBirthMap = new HashMap<>();
    private final String TeamsURL = "https://itnj1.webhook.office.com/webhookb2/647c0a1a-3c0b-4a00-a5e0-657658024ff3@1cbb409f-7344-429e-99c2-7d5266123c71/IncomingWebhook/7b300c7938fa47d8aa25deaa0d36aa07/e769085a-09d4-468e-a90d-a2154c82b5ca";
    private final String HolidayOpenURL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo";

    private final String ServiceKey = "GIg%2B6cR2BQpwkkfyaCJKV75pwxDbREX6EWbBYftWROxzo6cBJBnT7vmKcNo4hhxUj0JfTeMRg6wfJPwY820CRA%3D%3D";
    private final List<LocalDate> holiDayList = new ArrayList<>();

    @Autowired
    public BirthScheduler(RestTemplate restTemplate) throws InterruptedException {
        this.restTemplate = restTemplate;
        // 추후 DB 정보를 받아올 예정..
        injectHoliday();

        LocalDate today = LocalDate.now();
        int thisYear = today.getYear();

        changeBirthDay("이승연", LocalDate.of(thisYear,4,10));
        changeBirthDay("박웅옥", convertLunarToSolar(LocalDate.of(thisYear,1,1))); // 음력 변환 필요.
        changeBirthDay("양희종", LocalDate.of(thisYear,2,13));
        changeBirthDay("서승희", LocalDate.of(thisYear,3,21));
        changeBirthDay("최현순", LocalDate.of(thisYear,9,4));
        changeBirthDay("박찬미", LocalDate.of(thisYear,11,16));
        changeBirthDay("김사랑", LocalDate.of(thisYear,11,29));
        changeBirthDay("윤지연", LocalDate.of(thisYear,11,28));
        changeBirthDay("남원진", convertLunarToSolar(LocalDate.of(thisYear,9,3))); // 음력 변환 필요.
        changeBirthDay("김진규", LocalDate.of(thisYear,12,12));
        changeBirthDay("김건우", LocalDate.of(thisYear,3,31));
        changeBirthDay("최진휘", LocalDate.of(thisYear,1,15));
        changeBirthDay("정민찬", LocalDate.of(thisYear,2,14));
        changeBirthDay("조윤지", LocalDate.of(thisYear,11,19));
        changeBirthDay("정근준", LocalDate.of(thisYear,4,13));
        changeBirthDay("김예인", LocalDate.of(thisYear,7,12));
        changeBirthDay("김규민", LocalDate.of(thisYear,11,10));
        changeBirthDay("김진영", LocalDate.of(thisYear,11,17));
//        changeBirthDay("채민규", LocalDate.of(thisYear,11,9));
        changeBirthDay("윤보민", LocalDate.of(thisYear,10,16));
//        changeBirthDay("배소정", LocalDate.of(thisYear,3,2));
        changeBirthDay("손원철", LocalDate.of(thisYear,2,11));
        changeBirthDay("김예진", LocalDate.of(thisYear,10,24));



        realBirthMap.put("이승연", LocalDate.of(thisYear,4,10));
        realBirthMap.put("박웅옥", convertLunarToSolar(LocalDate.of(thisYear,1,1))); // 음력 변환 필요.
        realBirthMap.put("양희종", LocalDate.of(thisYear,2,13));
        realBirthMap.put("서승희", LocalDate.of(thisYear,3,21));
        realBirthMap.put("최현순", LocalDate.of(thisYear,9,4));
        realBirthMap.put("박찬미", LocalDate.of(thisYear,11,16));
        realBirthMap.put("김사랑", LocalDate.of(thisYear,11,29));
        realBirthMap.put("윤지연", LocalDate.of(thisYear,11,28));
        realBirthMap.put("남원진", convertLunarToSolar(LocalDate.of(thisYear,9,3))); // 음력 변환 필요.
        realBirthMap.put("김진규", LocalDate.of(thisYear,12,12));
        realBirthMap.put("김건우", LocalDate.of(thisYear,3,31));
        realBirthMap.put("최진휘", LocalDate.of(thisYear,1,15));
        realBirthMap.put("정민찬", LocalDate.of(thisYear,2,14));
        realBirthMap.put("조윤지", LocalDate.of(thisYear,11,19));
        realBirthMap.put("정근준", LocalDate.of(thisYear,4,13));
        realBirthMap.put("김예인", LocalDate.of(thisYear,7,12));
        realBirthMap.put("김규민", LocalDate.of(thisYear,11,10));
        realBirthMap.put("김진영", LocalDate.of(thisYear,11,17));
//        realBirthMap.put("채민규", LocalDate.of(thisYear,11,9));
        realBirthMap.put("윤보민", LocalDate.of(thisYear,10,16));
//        realBirthMap.put("배소정", LocalDate.of(thisYear,3,2));
        realBirthMap.put("손원철", LocalDate.of(thisYear,2,11));
        realBirthMap.put("김예진", LocalDate.of(thisYear, 10, 24));


        log.info("바뀐생일 = {}",birthMap);
        log.info("진짜생일 = {}",realBirthMap);
        log.info("휴일 = {}",holiDayList);

    }


    private void injectHoliday() throws InterruptedException {

        try {
            // Request Header 설정
            HttpHeaders headers = new HttpHeaders();

            // 오늘의 날짜를 가져옵니다.
            LocalDate today = LocalDate.now();
            int targetYear = today.getYear();


            URI uri = UriComponentsBuilder.fromHttpUrl(HolidayOpenURL)
                    .queryParam("ServiceKey", ServiceKey)
                    .queryParam("solYear", targetYear)
                    .queryParam("numOfRows", 100)
                    .build(true)
                    .toUri();

            HttpEntity entity = new HttpEntity<>(headers);

            log.info("url 보기 {}", uri);

            ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);


            log.info("휴일출력값 = {}",exchange.getBody());

            String responseBody = exchange.getBody();
            JSONObject jsonObject = new JSONObject(responseBody);

            // "response" 객체에서 "body" 객체를 가져온 후 그 안에 있는 "items" 배열을 가져옴
            JSONObject bodyObject = jsonObject.getJSONObject("response").getJSONObject("body");
            JSONArray itemsArray = bodyObject.getJSONObject("items").getJSONArray("item");

            log.info("제이슨 items ={}", itemsArray);

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                int locdate = itemObject.getInt("locdate");
                String locdateStr = String.valueOf(locdate);
                LocalDate localDate = LocalDate.parse(locdateStr, DateTimeFormatter.BASIC_ISO_DATE);
                holiDayList.add(localDate);

            }

        }catch (JSONException e){
            Thread.sleep(10000);
            injectHoliday();
        }




    }


    //    @Scheduled(fixedRate = 100000000)
    @Scheduled(cron = "0 30 15 * * ?")
    public void birthAlarm(){

        // 오늘의 날짜를 가져옵니다.
        LocalDate today = LocalDate.now();
        int targetMonth = today.getMonthValue();
        int targetDay = today.getDayOfMonth();


        // 1. 생일 날짜에 맞는 사람 출력,
        for(String name : birthMap.keySet()){
            LocalDate targetBirthDay = birthMap.get(name);

            int birthMonth = targetBirthDay.getMonthValue();
            int birthDay = targetBirthDay.getDayOfMonth();

            log.info("생일자 ={} , 변환일자 = {}", name, birthMap.get(name));

            if( birthMonth == targetMonth && birthDay == targetDay ){
                log.info("생일 알람시작! ={} ", new Date());

                LocalDate realDate = realBirthMap.get(name);

                String title = "생일 축하 알람 입니다 !";
                String text = String.format("%s 님 생일 축하드립니다! 조기퇴근 축하드립니다! \n 실제 생일은 %s 입니다 ㅎㅎ", name , realDate.toString());

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
            }


        }


    }


    // 공휴일 업데이트  일주일에 한번씩


    private void changeBirthDay(String name, LocalDate birthday) {
        // 주말인 경우, 이전 평일인 금요일의 날짜를 저장
        if (birthday.getDayOfWeek() == DayOfWeek.SATURDAY || birthday.getDayOfWeek() == DayOfWeek.SUNDAY) {
            changeBirthDay(name,birthday.minusDays(1));
        }else if(holiDayList.contains(birthday)){ // 공휴일일 경우 재귀 함수 실행!
            changeBirthDay(name,birthday.minusDays(1));
        }else {
            birthMap.put(name, birthday);
        }
    }


    private static LocalDate convertLunarToSolar(LocalDate birthday) {
        KoreanLunarCalendar calendar = KoreanLunarCalendar.getInstance();
        int year = birthday.getYear();
        int month = birthday.getMonth().getValue();
        int day = birthday.getDayOfMonth();

        calendar.setLunarDate(year,month, day,false);

        log.info("양력날짜변환 ={}",calendar.getSolarIsoFormat());

        // 2024-02-10
        String solarFormat = calendar.getSolarIsoFormat();
        int convertYear = Integer.parseInt(solarFormat.substring(0,4));
        int covertMonth = Integer.parseInt(solarFormat.substring(5,7));
        int covertDay = Integer.parseInt(solarFormat.substring(8,10));

        return LocalDate.of(convertYear,covertMonth,covertDay);
    }


}
