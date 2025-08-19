package com.example.suki.api.controller;

import com.example.suki.api.dto.*;
import com.example.suki.application.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Simulations")
@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
public class SimulationController {
    private final SimulationService simulationService;

    @Operation(summary = "성공 테스트")
    @GetMapping("/test/success")
    public ApiResponse<String> testSuccess() {
        return ApiResponse.ok("success");
    }

    @Operation(summary = "에러 테스트")
    @GetMapping("/test/error")
    public ApiResponse<String> testError() {
        throw new IllegalArgumentException("error");
    }

    @Operation(summary = "체력 n 달성 조합 반환")
    @PostMapping("/reach")
    public ApiResponse<SimulationResponse> simulateReach(@Valid @RequestBody SimulationRequest request) {
        return ApiResponse.ok(simulationService.simulateReach(request));
    }

    @Operation(summary = "체력 n으로 마무리 조합 반환")
    @PostMapping("/finish-at")
    public ApiResponse<SimulationResponse> simulateFinishAt(@Valid @RequestBody SimulationRequest request) {
        return ApiResponse.ok(simulationService.simulateFinishAt(request));
    }

    @Operation(summary = "체력 n1 ~ n2로 마무리 조합 반환")
    @PostMapping("/finish-within")
    public ApiResponse<SimulationRangeResponse> simulateFinishWithin(@Valid @RequestBody SimulationRangeRequest request) {
        return ApiResponse.ok(simulationService.simulateFinishWithin(request));
    }
}
