package com.example.suki.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common
    VALIDATION_FAILED("C001", HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    DUPLICATE_REQUEST("C002", HttpStatus.TOO_MANY_REQUESTS, "짧은 시간 내에 동일한 요청을 보낼 수 없습니다."),

    // User
    PLACE_ALREADY_ACTIVATED("U001", HttpStatus.CONFLICT, "이미 활성화된 장소입니다."),
    PLACE_ALREADY_DISABLED("U002", HttpStatus.CONFLICT, "이미 비활성화된 장소입니다."),

    // Place
    PLACE_REQUIRED("P001", HttpStatus.INTERNAL_SERVER_ERROR, "장소는 null일 수 없습니다."),
    ACTION_REQUIRED("P002", HttpStatus.INTERNAL_SERVER_ERROR, "행동은 null일 수 없습니다."),
    ACTION_ALREADY_DISABLED("P003", HttpStatus.INTERNAL_SERVER_ERROR, "이미 비활성화된 행동입니다."),

    // Algorithm
    ALGORITHM_NOT_FOUND("A001", HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 탐색 알고리즘입니다.");

    private final String code;
    private final HttpStatus status;
    private final String detail;
}
