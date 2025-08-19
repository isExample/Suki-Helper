package com.example.suki.api.advice;

import com.example.suki.api.exception.BusinessException;
import com.example.suki.api.exception.ErrorCode;
import com.example.suki.api.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse body = ErrorResponse.of(
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getCode(),
                errorCode.getDetail()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .orElse(errorCode.getDetail());

        ErrorResponse body = ErrorResponse.of(
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getCode(),
                detail
        );
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }


}
