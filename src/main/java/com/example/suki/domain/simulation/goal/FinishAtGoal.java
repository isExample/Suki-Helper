package com.example.suki.domain.simulation.goal;

public record FinishAtGoal(int target) implements Goal{
    @Override
    public boolean isSuccess(int tick, int stamina) {
        return tick == MAX_TICKS && stamina == target;
    }

    @Override
    public boolean isTerminal(int tick, int stamina) {
        return tick == MAX_TICKS || stamina == 0; // 중간에 목표 체력 달성해도 계속 탐색
    }
}
