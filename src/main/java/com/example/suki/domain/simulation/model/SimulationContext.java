package com.example.suki.domain.simulation.model;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.goal.Goal;

import java.util.List;

public record SimulationContext(
        UserState userState,
        Goal goal,
        int startTick,
        int startStamina,
        PlaceCategory secondPlace,
        DaySchedule schedule,
        ConsumableBag consumableBag,
        List<List<Tick>> solutions
) {}
