package com.example.suki.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Simulations")
@RestController
@RequestMapping("/api/v1/simulations")
public class SimulationController {

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
        return ApiResponse.ok(new SimulationResponse(0, true, null));
    }
}
