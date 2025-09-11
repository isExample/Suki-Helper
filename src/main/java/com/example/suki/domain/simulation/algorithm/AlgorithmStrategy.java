package com.example.suki.domain.simulation.algorithm;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.Tick;

import java.util.List;

public interface AlgorithmStrategy {
    int MAX_SOLUTIONS = 30;
    int MAX_TICKS = 14;
    int MAX_STAMINA = 100;
    int MIN_STAMINA = 0;

    boolean supports(AlgorithmType algorithmType);
    void solve(UserState userState, int currentTick, int currentStamina, Goal goal,
               PlaceCategory secondPlace, DaySchedule schedule, List<Tick> path, ConsumableBag consumableBag, List<List<Tick>> solutions);
}
