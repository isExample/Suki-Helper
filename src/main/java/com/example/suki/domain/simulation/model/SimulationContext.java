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
        ConsumableBag consumableBag,
        PlaceCategory secondPlace,
        DaySchedule schedule,
        List<List<Tick>> solutions
) {
    public SimulationContext updateExecutionContext(DaySchedule schedule, PlaceCategory secondPlace, List<List<Tick>> solutions) {
        return new SimulationContext(
                this.userState,
                this.goal,
                this.startTick,
                this.startStamina,
                this.consumableBag,
                secondPlace,
                schedule,
                solutions
        );
    }
}
