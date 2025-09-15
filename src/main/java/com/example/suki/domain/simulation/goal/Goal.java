package com.example.suki.domain.simulation.goal;

public interface Goal {
    int MAX_TICKS = 14;
    boolean isSuccess(int tick, int stamina);
    boolean isTerminal(int tick, int stamina, int maxTicks);
}
