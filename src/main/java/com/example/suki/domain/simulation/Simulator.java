package com.example.suki.domain.simulation;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.Place;
import com.example.suki.domain.place.PlaceCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class Simulator {
    private static final int MAX_STAMINA = 100;
    private static final int MIN_STAMINA = 0;
    private static final int MAX_TICKS = 14;
    private static final int WEEKDAY_SCHOOL_TICKS = 6;
    private static final TickSchedule WEEKEND_SCHEDULE = (tick, second) -> second;
    private static final TickSchedule WEEKDAY_SCHEDULE = (tick, second) -> (tick < WEEKDAY_SCHOOL_TICKS ? PlaceCategory.SCHOOL : second);

    public SimulationResult simulateReach(UserState userState, int targetStamina){
        return simulate(userState, new ReachGoal(targetStamina));
    }

    public SimulationResult simulate(UserState userState, Goal goal){
        switch (userState.getDay()) {
            case WEEKEND:
                return simulateBySchedule(userState, goal, WEEKEND_SCHEDULE);
            case WEEKDAY_MON:
            case WEEKDAY_OTHER:
                return simulateBySchedule(userState, goal, WEEKDAY_SCHEDULE);
            default:
                return SimulationResult.failure();
        }
    }

    private SimulationResult simulateBySchedule(UserState userState, Goal goal, TickSchedule schedule) {
        for (Map.Entry<PlaceCategory, Place> entry : userState.getPlaces().entrySet()) {
            PlaceCategory second = entry.getKey(); // 평일: 두번째 장소 / 주말: 단일 장소
            List<Tick> path = new ArrayList<>();
            if (findPath(userState, 0, MAX_STAMINA, goal, second, schedule, path)) {
                return SimulationResult.success(path);
            }
        }
        return SimulationResult.failure();
    }

    private boolean findPath(UserState userState, int currentTick, int currentStamina, Goal goal,
                             PlaceCategory secondPlace, TickSchedule schedule, List<Tick> path) {
        if(goal.isTerminal(currentTick, currentStamina, MAX_TICKS)){
            return goal.isSuccess(currentTick, currentStamina);
        }

        PlaceCategory place = schedule.placeAt(currentTick, secondPlace);
        Map<ActionCategory, Integer> actions = new EnumMap<>(userState.getPlaces().get(place).getActions());

        for (Map.Entry<ActionCategory, Integer> entry : actions.entrySet()) {
            ActionCategory action = entry.getKey();
            int delta = entry.getValue();

            int nextStamina = Math.min(MAX_STAMINA, currentStamina + delta);
            if (nextStamina <= MIN_STAMINA) {
                continue;
            }

            path.add(new Tick(place, action));
            if (findPath(userState, currentTick + 1, nextStamina, goal, place, schedule, path)) {
                return true;
            }
            path.remove(path.size() - 1);
        }
        return false;
    }
}
