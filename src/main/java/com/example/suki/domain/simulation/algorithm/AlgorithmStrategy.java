package com.example.suki.domain.simulation.algorithm;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.item.ConsumableItemCategory;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface AlgorithmStrategy {
    int MAX_SOLUTIONS = 10;
    int MAX_TICKS = 14;
    int MAX_STAMINA = 100;
    int MIN_STAMINA = 0;
    int WEEKDAY_SCHOOL_TICKS = 6;
    int INITIAL_STAMINA = 100;
    int INITIAL_TICK = 0;

    record SearchState(SearchState parent, Tick currentTick, int tick, int stamina, ConsumableBag bag, ActionCountKey actionCountKey) {
        public List<Tick> reconstructPath() {
            LinkedList<Tick> path = new LinkedList<>();
            SearchState current = this;
            while (current != null && current.currentTick() != null) {
                path.addFirst(current.currentTick());
                current = current.parent();
            }
            return path;
        }
    }

    record VisitedKey(int tick, int stamina, Map<ConsumableItemCategory, Integer> bagState, ActionCountKey actionCountKey) {}


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
