package com.example.suki.api.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int status,
        String title,
        String code,
        String detail
) {
    public static ErrorResponse of(HttpStatus status, String title, String code, String detail) {
        return new ErrorResponse(status.value(), title, code, detail);
    }
}
