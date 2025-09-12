package com.example.suki.domain.simulation.algorithm;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.item.ConsumableItemCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.Tick;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DfsAlgorithm implements AlgorithmStrategy{
    private record VisitedKey(int tick, int stamina, Map<ConsumableItemCategory, Integer> bagState) {}

    @Override
    public boolean supports(AlgorithmType algorithmType) {
        return algorithmType == AlgorithmType.DFS;
    }

    @Override
    public void solve(UserState userState, int currentTick, int currentStamina, Goal goal,
                      PlaceCategory secondPlace, DaySchedule schedule, List<Tick> path, ConsumableBag consumableBag, List<List<Tick>> solutions) {
        Set<VisitedKey> visitedStates = new HashSet<>();
        Set<ActionCountKey> uniqueCombinations = new HashSet<>();
        solveRecursive(userState, currentTick, currentStamina, goal, secondPlace, schedule, path, consumableBag, solutions, uniqueCombinations, visitedStates);
    }

    private void solveRecursive(UserState userState, int currentTick, int currentStamina, Goal goal,
                      PlaceCategory secondPlace, DaySchedule schedule, List<Tick> path, ConsumableBag consumableBag, List<List<Tick>> solutions, Set<ActionCountKey> uniqueCombinations, Set<VisitedKey> visitedStates) {
        VisitedKey currentKey = new VisitedKey(currentTick, currentStamina, consumableBag.snapshotRemains());
        if (!visitedStates.add(currentKey)) {
            return;
        }

        if(solutions.size() >= MAX_SOLUTIONS) {
            return;
        }

        if(goal.isTerminal(currentTick, currentStamina, MAX_TICKS)){
            if(goal.isSuccess(currentTick, currentStamina)){
                ActionCountKey combinationKey = ActionCountKey.from(path);
                if (uniqueCombinations.add(combinationKey)) {
                    solutions.add(List.copyOf(path));
                }
            }
            return;
        }

        PlaceCategory place = schedule.placeAt(currentTick, secondPlace);
        Map<ActionCategory, Integer> actions = new EnumMap<>(userState.getPlaces().get(place).getActions());

        for (Map.Entry<ActionCategory, Integer> entry : actions.entrySet()) {
            ActionCategory action = entry.getKey();
            int delta = entry.getValue();

            int nextStamina = Math.min(MAX_STAMINA, currentStamina + delta);
            if (nextStamina <= MIN_STAMINA || nextStamina == MAX_STAMINA) {
                continue;
            }

            // 소비성 아이템 미사용
            path.add(new Tick(place, action, Math.abs(delta), null));
            solveRecursive(userState, currentTick + 1, nextStamina, goal, place, schedule, path, consumableBag, solutions, uniqueCombinations, visitedStates);
            path.remove(path.size() - 1);

            // 소비성 아이템 사용
            for(ConsumableItemCategory item : consumableBag.usableItems()){
                if(!consumableBag.canUse(item)) {
                    continue;
                }

                int itemNextStamina = item.apply(nextStamina);
                consumableBag.use(item);
                path.add(new Tick(place, action, Math.abs(delta), item));
                solveRecursive(userState, currentTick + 1, itemNextStamina, goal, place, schedule, path, consumableBag, solutions, uniqueCombinations, visitedStates);
                path.remove(path.size() - 1);
                consumableBag.undo(item);
            }
        }
    }
}
