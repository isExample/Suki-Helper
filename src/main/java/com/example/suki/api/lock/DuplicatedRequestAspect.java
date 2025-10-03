package com.example.suki.api.lock;

import com.example.suki.api.exception.BusinessException;
import com.example.suki.api.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DuplicatedRequestAspect {
    private final ConcurrentHashMap<String, Lock> lockMap = new ConcurrentHashMap<>();

    @Around("@annotation(com.example.suki.api.lock.PreventDuplicateRequest)")
    public Object preventDuplicateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestKey = generateRequestKey(joinPoint);

        Lock lock = lockMap.computeIfAbsent(requestKey, k -> new ReentrantLock());

        boolean acquired = lock.tryLock();

        if (!acquired) {
            log.warn("Duplicate request detected: {}", requestKey);
            throw new BusinessException(ErrorCode.DUPLICATE_REQUEST);
        }

        try {
            return joinPoint.proceed();
        } finally {
            lock.unlock();
            lockMap.remove(requestKey);
        }
    }

    private String generateRequestKey(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String userIdentifier = request.getSession().getId(); // 세션 ID로 사용자 식별
        log.debug("User: {}", userIdentifier);

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
}
