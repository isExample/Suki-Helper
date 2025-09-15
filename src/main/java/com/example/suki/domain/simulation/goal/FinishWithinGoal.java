package com.example.suki.domain.simulation.goal;

public record FinishWithinGoal(int min, int max) implements Goal{
    @Override
    public boolean isSuccess(int tick, int stamina) {
        return tick == MAX_TICKS && stamina >= min && stamina <= max;
    }

    @Override
    public boolean isTerminal(int tick, int stamina) {
        return tick == MAX_TICKS || stamina == 0;
    }
}
