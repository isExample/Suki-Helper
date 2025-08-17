package com.example.suki.api.dto;

import com.example.suki.domain.simulation.model.SimulationResult;
import com.example.suki.domain.simulation.model.Tick;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SimulationRangeResponse(
        @Schema(description = "목표 체력 최소값")
        int targetMin,
        @Schema(description = "목표 체력 최대값")
        int targetMax,
        @Schema(description = "목표 체력 달성 가능 여부")
        boolean isPossible,
        @Schema(description = "목표 체력 달성 조합")
        List<Tick> tickList
) {
    public static SimulationRangeResponse from(int targetMin, int targetMax, SimulationResult result) {
        return new SimulationRangeResponse(
                targetMin,
                targetMax,
                result.isPossible(),
                result.getTickList()
        );
    }
}
