package com.example.suki.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    PLACE_ALREADY_ACTIVATED("P001", HttpStatus.CONFLICT, "이미 활성화된 장소입니다.");

    private final String code;
    private final HttpStatus status;
    private final String detail;
}
