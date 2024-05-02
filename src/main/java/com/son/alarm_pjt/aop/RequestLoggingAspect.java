package com.son.alarm_pjt.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class RequestLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);
    private static String type = "";

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), String.join(",", entry.getValue())))
                .collect(Collectors.joining(", "));
    }


    @Pointcut("within(com.son.alarm_pjt.controller..*)") // 3
    public void onRequest() {
    }


    @Around("com.son.alarm_pjt.aop.RequestLoggingAspect.onRequest()") // 4
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }



        long start = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs()); // 6
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Request: {} ,{} , {}, parameter : {} ,({}ms)", request.getMethod(), request.getRemoteHost(), request.getRequestURI(),
                    params,  end - start);
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            logger.error("Error reading request body", e);
            return "";
        }
    }
@Before( "execution(* com..*Service.*(..))")
public void before(JoinPoint j) {
    type = j.getSignature().getDeclaringTypeName();
    boolean isPass = j.getSignature().getName().compareTo("getFileInfo") == 0;
    if (type.indexOf("Service") > -1 && !isPass) {
        logger.info("===========================================\trestAPI Service 시작\t=====================================");
        logger.info(j.getTarget()+"");
        logger.info("Method:\t"+j.getSignature().getName());
        Object args[] = j.getArgs();
        if(args!=null){
            for (int i = 0; i < args.length; i++) {
                logger.info(i+" 번째 값 :\t"+args[i]);
            }
        }
    }

}

}
