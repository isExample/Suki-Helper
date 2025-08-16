package com.example.suki.domain.simulation.goal;

public record ReachGoal(int target) implements Goal {
    @Override
    public boolean isSuccess(int tick, int stamina) {
        return stamina == target;
    }

    @Override
    public boolean isTerminal(int tick, int stamina, int maxTicks) {
        return tick == maxTicks || stamina == 0 || stamina == target;
    }
}
