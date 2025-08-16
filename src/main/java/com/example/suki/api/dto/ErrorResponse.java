package com.example.suki.api.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int status, String message) {
    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), message);
    }
}
