package com.example.suki.api;

import com.example.suki.application.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping
    public ApiResponse<SimulationResponse> simulate(@RequestBody SimulationRequest request) {
        return ApiResponse.ok(simulationService.applyModifiers(request));
    }
}
