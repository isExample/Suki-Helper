package com.example.suki.domain.simulation.model;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class SimulationResult {
    private final boolean isPossible;
    private final List<Tick> tickList;

    public SimulationResult(boolean isPossible, List<Tick> tickList) {
        this.isPossible = isPossible;
        this.tickList = (tickList != null) ? Collections.unmodifiableList(tickList) : null;
    }

    public static SimulationResult success(List<Tick> tickList) {
        return new SimulationResult(true, tickList);
    }

    public static SimulationResult failure() {
        return new SimulationResult(false, null);
    }
}
