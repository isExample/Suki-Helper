package com.example.suki.domain.simulation.algorithm;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface AlgorithmStrategy {
    int MAX_SOLUTIONS = 10;
    int MAX_TICKS = 14;
    int MAX_STAMINA = 100;
    int MIN_STAMINA = 0;
    int INITIAL_STAMINA = 100;
    int INITIAL_TICK = 0;

    record ActionCountKey(Map<ActionCategory, Long> counts) {
        public static ActionCountKey from(List<Tick> path) {
            Map<ActionCategory, Long> counts = path.stream()
                    .collect(Collectors.groupingBy(Tick::action, Collectors.counting()));
            return new ActionCountKey(counts);
        }

        public ActionCountKey add(ActionCategory action) {
            Map<ActionCategory, Long> newCounts = new EnumMap<>(this.counts);
            newCounts.merge(action, 1L, Long::sum);
            return new ActionCountKey(newCounts);
        }
    }

    boolean supports(AlgorithmType algorithmType);

    int solve(SimulationContext context);
}
