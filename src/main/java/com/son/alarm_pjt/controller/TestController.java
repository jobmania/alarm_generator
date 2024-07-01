package com.son.alarm_pjt.controller;


import com.son.alarm_pjt.domain.Cleaning;
import com.son.alarm_pjt.domain.ResponseDto;
import com.son.alarm_pjt.domain.response.ListDto;
import com.son.alarm_pjt.service.MongoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final MongoService mongoService;
    @PostMapping("/in")
    public boolean testInput(){
        return mongoService.insertTest();
    }

    @PostMapping ("/generate")
    public ResponseDto<?> generate(){
        return mongoService.insertCleaningData();
    }

/************/

    @GetMapping("/task")
    public ResponseDto<?> insertTask(@RequestParam(name = "date") String date){
        List<ListDto> tasks = mongoService.getTasks(date);
        return ResponseDto.success(tasks,date+"일자 청소목록");
    }

    @DeleteMapping("/member")
    public ResponseDto<?> deleteMember(@RequestParam(name = "name") String name){
        mongoService.deleteMember(name);
        return ResponseDto.success("삭제","완료");
    }


    @PostMapping("/cleaning")
    public ResponseDto<?> insertCleaning(
            @RequestParam(name = "date") String date,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "task") String task
    ){
        Cleaning cleaning = mongoService.insertCleaning(date, name, task);
        return ResponseDto.success(cleaning,"완료");
    }
}
