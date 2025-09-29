package com.example.suki.api.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SimulationLoggingAspect {
    private final ObjectMapper objectMapper;

    @Pointcut("within(com.example.suki.api.controller.SimulationController)")
    public void simulationControllerPointcut() {
    }

    @Around("simulationControllerPointcut()") //
    public Object logSimulationRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        // Request Body 로깅
        Object requestBody = joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null;
        log.info(objectMapper.writeValueAsString(Map.of(
                "type", "API_REQUEST",
                "http_method", method,
                "uri", requestURI,
                "payload", requestBody
        )));

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 실제 메서드 실행
            result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            // Response Body 로깅
            log.info(objectMapper.writeValueAsString(Map.of(
                    "type", "API_RESPONSE",
                    "http_method", method,
                    "uri", requestURI,
                    "duration_ms", duration,
                    "payload", result
            )));
        }
    }
}
