package com.example.suki.domain.simulation.algorithm;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.simulation.model.SimulationContext;

import java.util.Arrays;

public interface AlgorithmStrategy {
    int MAX_SOLUTIONS = 10;
    int MAX_TICKS = 14;
    int MAX_STAMINA = 100;
    int MIN_STAMINA = 0;
    int INITIAL_STAMINA = 100;
    int INITIAL_TICK = 0;

    record ActionCountKey(int[] counts) {
        public static ActionCountKey create() {
            return new ActionCountKey(new int[ActionCategory.values().length]);
        }

        public ActionCountKey add(ActionCategory action) {
            int[] newCounts = this.counts.clone();
            newCounts[action.ordinal()]++;
            return new ActionCountKey(newCounts);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ActionCountKey that = (ActionCountKey) o;

            return Arrays.equals(counts, that.counts); // JVM 레벨에서 최적화된 Arrays.equals를 사용
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(counts);
        }
    }

    boolean supports(AlgorithmType algorithmType);

    int solve(SimulationContext context);
}
