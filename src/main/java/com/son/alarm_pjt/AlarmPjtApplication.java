package com.son.alarm_pjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAspectJAutoProxy // AOP
@EnableAsync // 이벤트를위한 비동기 처리.
@EnableScheduling // 스케줄링
public class AlarmPjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmPjtApplication.class, args);
	}


	/**
	 * RestTemplate 등록...
	 * */
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
