package com.example.suki.domain.simulation.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SimulationResult {
    private final boolean isPossible;
    private final List<List<Tick>> combinations;

    public SimulationResult(boolean isPossible, List<List<Tick>> combinations) {
        this.isPossible = isPossible;
        this.combinations = (combinations != null) ? Collections.unmodifiableList(combinations) : null;
    }

    public static SimulationResult success(List<List<Tick>> combinations) {
        return new SimulationResult(true, combinations);
    }

    public static SimulationResult failure() {
        return new SimulationResult(false, new ArrayList<>());
    }
}
