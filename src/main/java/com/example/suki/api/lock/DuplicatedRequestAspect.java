package com.example.suki.api.lock;

import com.example.suki.api.exception.BusinessException;
import com.example.suki.api.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DuplicatedRequestAspect {
    private static final ConcurrentHashMap<String, Object> requestLocks = new ConcurrentHashMap<>();
    private static final Object DUMMY_VALUE = new Object();

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Around("@annotation(com.example.suki.api.lock.PreventDuplicateRequest)")
    public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestKey = generateRequestKey(joinPoint);

        if (requestLocks.putIfAbsent(requestKey, DUMMY_VALUE) != null) {
            log.warn("Duplicate request detected: {}", requestKey);
            throw new BusinessException(ErrorCode.DUPLICATE_REQUEST);
        }

        try {
            return joinPoint.proceed();
        } finally {
            requestLocks.remove(requestKey);
        }
    }

    private String generateRequestKey(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String userIdentifier;
        if ("perf".equalsIgnoreCase(activeProfile)) {
            userIdentifier = UUID.randomUUID().toString();
            log.debug("Test profile active. Using random UUID for user identifier: {}", userIdentifier);
        } else{
            if (request.getSession(false) != null && !request.getSession().isNew()) {
                userIdentifier = request.getSession().getId();
            } else {
                userIdentifier = getClientIp(request);
            }
            log.debug("User: {}", userIdentifier);
        }

        StringBuilder payloadBuilder = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                payloadBuilder.append(arg);
            }
        }

        // MD5 해시 -> 최종 키 생성 (사용자 ID + 요청 URL + 요청 본문 해시)
        String combined = userIdentifier + request.getRequestURI() + payloadBuilder;
        return DigestUtils.md5DigestAsHex(combined.getBytes());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("Forwarded");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
