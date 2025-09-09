package com.example.suki.domain.simulation;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.item.ConsumableItemCategory;
import com.example.suki.domain.place.Place;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.goal.FinishAtGoal;
import com.example.suki.domain.simulation.goal.FinishWithinGoal;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationResult;
import com.example.suki.domain.simulation.model.Tick;
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
    private static final int MAX_SOLUTIONS = 10;

    private static final DaySchedule WEEKEND_SCHEDULE = (tick, second) -> second;
    private static final DaySchedule WEEKDAY_SCHEDULE = (tick, second) -> (tick < WEEKDAY_SCHOOL_TICKS ? PlaceCategory.SCHOOL : second);

    public SimulationResult simulateReach(UserState userState, int targetStamina, Map<ConsumableItemCategory, Integer> consumableItemMap){
        return simulate(userState, new ReachGoal(targetStamina), new ConsumableBag(consumableItemMap));
    }

    public SimulationResult simulateFinishAt(UserState userState, int targetStamina, Map<ConsumableItemCategory, Integer> consumableItemMap){
        return simulate(userState, new FinishAtGoal(targetStamina), new ConsumableBag(consumableItemMap));
    }

    public SimulationResult simulateFinishWithin(UserState userState, int min, int max, Map<ConsumableItemCategory, Integer> consumableItemMap){
        return simulate(userState, new FinishWithinGoal(min, max), new ConsumableBag(consumableItemMap));
    }

    private SimulationResult simulate(UserState userState, Goal goal, ConsumableBag consumableBag){
        switch (userState.getDay()) {
            case WEEKEND:
                return simulateBySchedule(userState, goal, WEEKEND_SCHEDULE, consumableBag);
            case WEEKDAY_MON:
            case WEEKDAY_OTHER:
                return simulateBySchedule(userState, goal, WEEKDAY_SCHEDULE, consumableBag);
            default:
                return SimulationResult.failure();
        }
    }

    private SimulationResult simulateBySchedule(UserState userState, Goal goal, DaySchedule schedule, ConsumableBag consumableBag) {
        List<List<Tick>> solutions = new ArrayList<>();

        for (Map.Entry<PlaceCategory, Place> entry : userState.getPlaces().entrySet()) {
            PlaceCategory second = entry.getKey(); // 평일: 두번째 장소 / 주말: 단일 장소
            List<Tick> path = new ArrayList<>();
            findPath(userState, 0, MAX_STAMINA, goal, second, schedule, path, consumableBag, solutions);

            if(solutions.size() >= MAX_SOLUTIONS) {
                break;
            }
        }
        return solutions.isEmpty() ? SimulationResult.failure() : SimulationResult.success(solutions);
    }

    private void findPath(UserState userState, int currentTick, int currentStamina, Goal goal,
                             PlaceCategory secondPlace, DaySchedule schedule, List<Tick> path, ConsumableBag consumableBag, List<List<Tick>> solutions) {
        if(solutions.size() >= MAX_SOLUTIONS) {
            return;
        }

        if(goal.isTerminal(currentTick, currentStamina, MAX_TICKS)){
            if(goal.isSuccess(currentTick, currentStamina)){
                solutions.add(List.copyOf(path));
            }
            return;
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

            // 소비성 아이템 미사용
            path.add(new Tick(place, action, Math.abs(delta), null));
            findPath(userState, currentTick + 1, nextStamina, goal, place, schedule, path, consumableBag, solutions);
            path.remove(path.size() - 1);

            // 소비성 아이템 사용
            for(ConsumableItemCategory item : consumableBag.usableItems()){
                if(!consumableBag.canUse(item) || nextStamina == MAX_STAMINA) {
                    continue;
                }

                int itemNextStamina = item.apply(nextStamina);
                consumableBag.use(item);
                path.add(new Tick(place, action, Math.abs(delta), item));
                findPath(userState, currentTick + 1, itemNextStamina, goal, place, schedule, path, consumableBag, solutions);
                path.remove(path.size() - 1);
                consumableBag.undo(item);
            }
        }
    }
}
