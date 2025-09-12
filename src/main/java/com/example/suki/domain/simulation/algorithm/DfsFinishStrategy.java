package com.example.suki.domain.simulation.algorithm;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.item.ConsumableItemCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DfsFinishStrategy implements AlgorithmStrategy{
    private record RecursiveState(int tick, int stamina, ConsumableBag bag) {}
    private record VisitedKey(int tick, int stamina, Map<ConsumableItemCategory, Integer> bagState) {}

    @Override
    public boolean supports(AlgorithmType algorithmType) {
        return algorithmType == AlgorithmType.DFS_FINISH;
    }

    @Override
    public void solve(SimulationContext context) {
        Set<VisitedKey> visitedStates = new HashSet<>();
        Set<ActionCountKey> uniqueCombinations = new HashSet<>();

        RecursiveState initialState = new RecursiveState(INITIAL_TICK, INITIAL_STAMINA, context.consumableBag());
        solveRecursive(initialState, new ArrayList<>(), context, uniqueCombinations, visitedStates);
    }

    private void solveRecursive(RecursiveState currentState, List<Tick> path, SimulationContext context,
                                Set<ActionCountKey> uniqueCombinations, Set<VisitedKey> visitedStates) {
        int currentTick = currentState.tick();
        int currentStamina = currentState.stamina();
        ConsumableBag consumableBag = currentState.bag();

        VisitedKey currentKey = new VisitedKey(currentTick, currentStamina, consumableBag.snapshotRemains());
        if (!visitedStates.add(currentKey)) {
            return;
        }

        if(context.solutions().size() >= MAX_SOLUTIONS) {
            return;
        }

        if(context.goal().isTerminal(currentTick, currentStamina, MAX_TICKS)){
            if(context.goal().isSuccess(currentTick, currentStamina)){
                ActionCountKey combinationKey = ActionCountKey.from(path);
                if (uniqueCombinations.add(combinationKey)) {
                    context.solutions().add(List.copyOf(path));
                }
            }
            return;
        }

        PlaceCategory place = context.schedule().placeAt(currentTick, context.secondPlace());
        Map<ActionCategory, Integer> actions = new EnumMap<>(context.userState().getPlaces().get(place).getActions());

        for (Map.Entry<ActionCategory, Integer> entry : actions.entrySet()) {
            ActionCategory action = entry.getKey();
            int delta = entry.getValue();

            int nextStamina = Math.min(MAX_STAMINA, currentStamina + delta);
            if (nextStamina <= MIN_STAMINA || nextStamina == MAX_STAMINA) {
                continue;
            }

            // 소비성 아이템 미사용
            path.add(new Tick(place, action, Math.abs(delta), null));
            RecursiveState nextStateNoItem = new RecursiveState(currentTick + 1, nextStamina, consumableBag);
            solveRecursive(nextStateNoItem, path, context, uniqueCombinations, visitedStates);
            path.remove(path.size() - 1);

            // 소비성 아이템 사용
            for(ConsumableItemCategory item : consumableBag.usableItems()){
                if(!consumableBag.canUse(item)) {
                    continue;
                }

                int itemNextStamina = item.apply(nextStamina);
                consumableBag.use(item);
                path.add(new Tick(place, action, Math.abs(delta), item));
                RecursiveState nextStateWithItem = new RecursiveState(currentTick + 1, itemNextStamina, consumableBag);
                solveRecursive(nextStateWithItem, path, context, uniqueCombinations, visitedStates);
                path.remove(path.size() - 1);
                consumableBag.undo(item);
            }
        }
    }
}
