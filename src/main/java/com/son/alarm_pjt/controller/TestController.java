package com.son.alarm_pjt.controller;


import com.son.alarm_pjt.domain.ResponseDto;
import com.son.alarm_pjt.service.MongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return mongoService.getTest();
    }
}
