package com.example.suki.api.controller;

import com.example.suki.api.dto.ApiResponse;
import com.example.suki.api.dto.SupportRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Support")
@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportController {
    @Operation(summary = "피드백 및 문의 제출")
    @PostMapping
    public ApiResponse<String> simulateReach(@Valid @RequestBody SupportRequest request) {
        return ApiResponse.ok("피드백 및 문의가 전송되었습니다.");
    }
}
