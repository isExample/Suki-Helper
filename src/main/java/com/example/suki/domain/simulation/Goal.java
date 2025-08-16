package com.example.suki.domain.simulation;

public interface Goal {
    boolean isSuccess(int tick, int stamina);
    boolean isTerminal(int tick, int stamina, int maxTicks);
}
