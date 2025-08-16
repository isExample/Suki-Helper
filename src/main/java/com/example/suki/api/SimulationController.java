package com.example.suki.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simulations")
public class SimulationController {
    @GetMapping("/test/success")
    public ApiResponse<String> testSuccess() {
        return ApiResponse.ok("success");
    }

    @GetMapping("/test/error")
    public ApiResponse<String> testError() {
        throw new IllegalArgumentException("error");
    }
}
