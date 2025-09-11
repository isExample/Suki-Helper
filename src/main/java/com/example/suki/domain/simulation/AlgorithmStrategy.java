package com.example.suki.domain.simulation;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.Tick;

import java.util.List;

public interface AlgorithmStrategy {
    void solve(UserState userState, int currentTick, int currentStamina, Goal goal,
               PlaceCategory secondPlace, DaySchedule schedule, List<Tick> path, ConsumableBag consumableBag, List<List<Tick>> solutions);
}
