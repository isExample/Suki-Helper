package com.example.suki.api.log;

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

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SimulationLoggingAspect {
    @Pointcut("within(com.example.suki.api.controller.SimulationController)")
    public void simulationControllerPointcut() {
    }

    @Around("simulationControllerPointcut()") //
    public Object logSimulationRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        Object requestBody = joinPoint.getArgs().length > 0 ? joinPoint.getArgs()[0] : null;
        log.info("API Request. Method: {}, URI: {}, Payload: {}", method, requestURI, requestBody);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // 실제 메서드 실행
            result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("API Response. Duration: {}ms, Payload: {}", duration, result);
        }
    }
}
