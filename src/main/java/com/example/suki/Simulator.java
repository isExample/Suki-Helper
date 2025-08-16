package com.example.suki;

import com.example.suki.domain.TickSchedule;
import com.example.suki.domain.SimulationResult;
import com.example.suki.domain.Tick;
import com.example.suki.domain.UserState;
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

    public SimulationResult simulate(UserState userState, int targetStamina){
        if(targetStamina < 1 || targetStamina > 99) throw new IllegalArgumentException("목표 체력은 1 이상 99 이하여야 합니다.");

        switch (userState.getDay()) {
            case WEEKEND:
                return simulateWeekend(userState, targetStamina);
            case WEEKDAY_MON:
            case WEEKDAY_OTHER:
                return simulateWeekday(userState, targetStamina);
        }

        return SimulationResult.failure();
    }

    private SimulationResult simulateWeekend(UserState userState, int targetStamina) {
        for (Map.Entry<PlaceCategory, Place> entry : userState.getPlaces().entrySet()) {
            PlaceCategory single = entry.getKey(); // 단일 장소 고정
            List<Tick> path = new ArrayList<>();
            if (findPath(userState, 0, MAX_STAMINA, targetStamina, single, WEEKEND_SCHEDULE, path)) {
                return SimulationResult.success(path);
            }
        }
        return SimulationResult.failure();
    }

    private SimulationResult simulateWeekday(UserState userState, int targetStamina) {
        for (Map.Entry<PlaceCategory, Place> entry : userState.getPlaces().entrySet()) {
            PlaceCategory second = entry.getKey(); // 첫 장소 학교 고정, 두 번째 장소 탐색
            List<Tick> path = new ArrayList<>();
            if (findPath(userState, 0, MAX_STAMINA, targetStamina, second, WEEKDAY_SCHEDULE, path)) {
                return SimulationResult.success(path);
            }
        }
        return SimulationResult.failure();
    }

    private boolean findPath(UserState userState, int currentTick, int currentStamina, int targetStamina,
                             PlaceCategory secondPlace, TickSchedule schedule, List<Tick> path) {
        if(currentTick == MAX_TICKS || currentStamina == targetStamina){
            return currentStamina == targetStamina;
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
            if (findPath(userState, currentTick + 1, nextStamina, targetStamina, place, schedule, path)) {
                return true;
            }

            path.remove(path.size() - 1);
        }

        return false;
    }
}
