package com.example.suki.api.dto;

import com.example.suki.domain.simulation.model.SimulationResult;
import com.example.suki.domain.simulation.model.Tick;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SimulationResponse(
        @Schema(description = "목표 체력")
        int targetStamina,
        @Schema(description = "목표 체력 달성 가능 여부")
        boolean isPossible,
        @Schema(description = "목표 체력 달성 조합")
        List<Tick> tickList
) {
    public static SimulationResponse from(int targetStamina, SimulationResult result) {
        return new SimulationResponse(
                targetStamina,
                result.isPossible(),
                result.getTickList()
        );
    }
}
