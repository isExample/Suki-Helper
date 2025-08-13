package com.example.suki;

import com.example.suki.domain.SimulationResult;
import com.example.suki.domain.Tick;
import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.Place;
import com.example.suki.domain.place.PlaceCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Simulator {
    private static final int MAX_TICKS = 14;
    private static final int MAX_STAMINA = 100;
    private static final int MIN_STAMINA = 0;

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
            List<Tick> combination = new ArrayList<>();
            if (findPath(0, MAX_STAMINA, targetStamina, entry.getKey(), entry.getValue().getActions(), combination)) {
                return SimulationResult.success(combination);
            }
        }
        return SimulationResult.failure();
    }

    private SimulationResult simulateWeekday(UserState userState, int targetStamina) {
        // 첫 장소 학교로 고정
        Place school = userState.getPlaces().get(PlaceCategory.SCHOOL);
        Map<ActionCategory, Integer> schoolActions = school.getActions();

        // 두 번째 장소 결정
        for (Map.Entry<PlaceCategory, Place> entry : userState.getPlaces().entrySet()) {
            PlaceCategory secondPlace = entry.getKey();
            List<Tick> combination = new ArrayList<>();
            if (findWeekdayPath(0, MAX_STAMINA, targetStamina, secondPlace, schoolActions, entry.getValue().getActions(), combination)) {
                return SimulationResult.success(combination);
            }
        }
        return SimulationResult.failure();
    }

    private boolean findPath(int currentTick, int currentStamina, int targetStamina,
                             PlaceCategory place, Map<ActionCategory, Integer> availableActions, List<Tick> combination) {
//        System.out.println("진입: " + currentTick + ", " + currentStamina);
        if(currentTick == MAX_TICKS || currentStamina == targetStamina){
            return currentStamina == targetStamina;
        }

        for (Map.Entry<ActionCategory, Integer> entry : availableActions.entrySet()) {
            ActionCategory action = entry.getKey();
            int delta = entry.getValue();

            int nextStamina = Math.min(MAX_STAMINA, currentStamina + delta);
            if (nextStamina <= MIN_STAMINA) {
                continue;
            }
//            System.out.println("반영: " + nextStamina + ", " + delta);
            combination.add(new Tick(place, action));
            if (findPath(currentTick + 1, nextStamina, targetStamina, place, availableActions, combination)) {
                return true;
            }

            combination.remove(combination.size() - 1);
        }

        return false;
    }

    private boolean findWeekdayPath(int currentTick, int currentStamina, int targetStamina,
                                    PlaceCategory secondPlace, Map<ActionCategory, Integer> schoolActions, Map<ActionCategory, Integer> secondActions,
                                    List<Tick> combination) {
        if (currentTick == MAX_TICKS || currentStamina == targetStamina) {
            return currentStamina == targetStamina;
        }

        boolean schoolPhase = currentTick < 6; // 첫 6틱은 학교 고정
        Map<ActionCategory, Integer> actions = schoolPhase ? schoolActions : secondActions;
        PlaceCategory place = schoolPhase ? PlaceCategory.SCHOOL : secondPlace;

        for (Map.Entry<ActionCategory, Integer> e : actions.entrySet()) {
            ActionCategory action = e.getKey();
            int delta = e.getValue();

            int nextStamina = Math.min(MAX_STAMINA, currentStamina + delta);
            if (nextStamina <= MIN_STAMINA) {
                continue;
            }

            combination.add(new Tick(place, action));
            if (findWeekdayPath(currentTick + 1, nextStamina, targetStamina, secondPlace, schoolActions, secondActions, combination)) {
                return true;
            }
            combination.remove(combination.size() - 1);
        }
        return false;
    }
}
