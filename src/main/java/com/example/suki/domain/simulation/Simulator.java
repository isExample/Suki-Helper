package com.example.suki.domain.simulation;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.item.ConsumableItemCategory;
import com.example.suki.domain.place.Place;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.goal.FinishAtGoal;
import com.example.suki.domain.simulation.goal.FinishWithinGoal;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.SimulationResult;
import com.example.suki.domain.simulation.model.Tick;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Simulator {
    private static final int WEEKDAY_SCHOOL_TICKS = 6;

    private static final DaySchedule WEEKEND_SCHEDULE = (tick, second) -> second;
    private static final DaySchedule WEEKDAY_SCHEDULE = (tick, second) -> (tick < WEEKDAY_SCHOOL_TICKS ? PlaceCategory.SCHOOL : second);

    public SimulationResult simulateReach(UserState userState, int targetStamina, int currentTick, int currentStamina, Map<ConsumableItemCategory, Integer> consumableItemMap, AlgorithmStrategy strategy){
        return simulate(userState, new ReachGoal(targetStamina), currentTick, currentStamina, new ConsumableBag(consumableItemMap), strategy);
    }

    public SimulationResult simulateFinishAt(UserState userState, int targetStamina, int currentTick, int currentStamina, Map<ConsumableItemCategory, Integer> consumableItemMap, AlgorithmStrategy strategy){
        return simulate(userState, new FinishAtGoal(targetStamina), currentTick, currentStamina, new ConsumableBag(consumableItemMap), strategy);
    }

    public SimulationResult simulateFinishWithin(UserState userState, int min, int max, int currentTick, int currentStamina, Map<ConsumableItemCategory, Integer> consumableItemMap, AlgorithmStrategy strategy){
        return simulate(userState, new FinishWithinGoal(min, max), currentTick, currentStamina, new ConsumableBag(consumableItemMap), strategy);
    }

    private SimulationResult simulate(UserState userState, Goal goal, int currentTick, int currentStamina, ConsumableBag consumableBag, AlgorithmStrategy strategy){
        switch (userState.getDay()) {
            case WEEKEND:
                return simulateBySchedule(userState, goal, currentTick, currentStamina, WEEKEND_SCHEDULE, consumableBag, strategy);
            case WEEKDAY_MON:
            case WEEKDAY_OTHER:
                return simulateBySchedule(userState, goal, currentTick, currentStamina, WEEKDAY_SCHEDULE, consumableBag, strategy);
            default:
                return SimulationResult.failure();
        }
    }

    private SimulationResult simulateBySchedule(UserState userState, Goal goal, int currentTick, int currentStamina, DaySchedule schedule, ConsumableBag consumableBag, AlgorithmStrategy strategy) {
        List<List<Tick>> solutions = new ArrayList<>();

        for (Map.Entry<PlaceCategory, Place> entry : userState.getPlaces().entrySet()) {
            PlaceCategory second = entry.getKey(); // 평일: 두번째 장소 / 주말: 단일 장소
            SimulationContext context = new SimulationContext(
                    userState,
                    goal,
                    currentTick,
                    currentStamina,
                    second,
                    schedule,
                    consumableBag,
                    solutions
            );
            strategy.solve(context);

            if(solutions.size() >= AlgorithmStrategy.MAX_SOLUTIONS) {
                break;
            }
        }
        return solutions.isEmpty() ? SimulationResult.failure() : SimulationResult.success(solutions);
    }
}
