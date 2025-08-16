package com.example.suki.api;

import com.example.suki.domain.Tick;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SimulationResponse(
        @Schema(description="목표 체력")
        int targetStamina,
        @Schema(description = "목표 체력 달성 가능 여부")
        boolean isPossible,
        @Schema(description = "목표 체력 달성 조합")
        List<Tick> tickList
) {
}
