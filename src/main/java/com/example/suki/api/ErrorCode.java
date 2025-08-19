package com.example.suki.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_FAILED("C001", HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),

    PLACE_ALREADY_ACTIVATED("P001", HttpStatus.CONFLICT, "이미 활성화된 장소입니다.");

    private final String code;
    private final HttpStatus status;
    private final String detail;
}
