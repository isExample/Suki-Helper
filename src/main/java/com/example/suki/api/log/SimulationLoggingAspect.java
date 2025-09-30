package com.example.suki.api.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import static net.logstash.logback.argument.StructuredArguments.kv;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SimulationLoggingAspect {
    private static final Marker API_REQUEST_MARKER = MarkerFactory.getMarker("API_REQUEST");
    private static final Marker API_RESPONSE_MARKER = MarkerFactory.getMarker("API_RESPONSE");

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
        log.info(API_REQUEST_MARKER, "API Request received",
                kv("http_method", method),
                kv("uri", requestURI),
                kv("payload", requestBody)
        );

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 실제 메서드 실행
            result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            // Response Body 로깅
            log.info(API_RESPONSE_MARKER, "API Response sent",
                    kv("http_method", method),
                    kv("uri", requestURI),
                    kv("duration_ms", duration),
                    kv("payload", result)
            );
        }
    }
}
