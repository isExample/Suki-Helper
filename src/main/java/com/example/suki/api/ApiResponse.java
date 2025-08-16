package com.example.suki.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "API 표준 응답 래퍼")
public record ApiResponse<T>(
        @Schema(description = "HTTP 상태 코드")
        int status,
        @Schema(description = "응답 메시지")
        String message,
        @Schema(description = "응답 데이터")
        T data
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "OK", data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), message, data);
    }
}
