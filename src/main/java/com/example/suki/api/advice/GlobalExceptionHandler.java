package com.example.suki.api.advice;

import com.example.suki.api.BusinessException;
import com.example.suki.api.ErrorCode;
import com.example.suki.api.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse body = ErrorResponse.of(
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getCode(),
                errorCode.getDetail()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }
}
