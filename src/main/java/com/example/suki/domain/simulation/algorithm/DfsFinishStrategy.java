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
    private record SearchState(int tick, int stamina, List<Tick> path, ConsumableBag bag, ActionCountKey actionCountKey) {}
    private record VisitedKey(int tick, int stamina, Map<ConsumableItemCategory, Integer> bagState, ActionCountKey actionCountKey) {}

    @Override
    public boolean supports(AlgorithmType algorithmType) {
        return algorithmType == AlgorithmType.DFS_FINISH;
    }

    @Override
    public int solve(SimulationContext context) {
        Set<VisitedKey> visitedStates = new HashSet<>();
        Set<ActionCountKey> uniqueCombinations = new HashSet<>();

        ActionCountKey initialActionCount = ActionCountKey.create();
        SearchState initialState = new SearchState(INITIAL_TICK, INITIAL_STAMINA, new ArrayList<>(), context.consumableBag(), initialActionCount);

        solveRecursive(initialState, context, uniqueCombinations, visitedStates);
        return visitedStates.size();
    }

    private void solveRecursive(SearchState currentState, SimulationContext context,
                                Set<ActionCountKey> uniqueCombinations, Set<VisitedKey> visitedStates) {
        int currentTick = currentState.tick();
        int currentStamina = currentState.stamina();
        ConsumableBag consumableBag = currentState.bag();
        ActionCountKey currentActionCount = currentState.actionCountKey();

        VisitedKey currentKey = new VisitedKey(currentTick, currentStamina, consumableBag.snapshotRemains(), currentActionCount);
        if (!visitedStates.add(currentKey)) {
            return;
        }

        if(context.solutions().size() >= MAX_SOLUTIONS) {
            return;
        }

        if(context.goal().isTerminal(currentTick, currentStamina)){
            if(context.goal().isSuccess(currentTick, currentStamina)){
                if (uniqueCombinations.add(currentActionCount)) {
                    context.solutions().add(List.copyOf(currentState.path()));
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

            ActionCountKey nextActionCount = currentActionCount.add(action);

            // 소비성 아이템 미사용
            List<Tick> newPathNoItem = new ArrayList<>(currentState.path());
            newPathNoItem.add(new Tick(place, action, Math.abs(delta), null));
            SearchState nextStateNoItem = new SearchState(currentTick + 1, nextStamina, newPathNoItem, consumableBag, nextActionCount);
            solveRecursive(nextStateNoItem, context, uniqueCombinations, visitedStates);

            // 소비성 아이템 사용
            for(ConsumableItemCategory item : consumableBag.usableItems()){
                if(!consumableBag.canUse(item)) {
                    continue;
                }

                ConsumableBag nextBagWithItem = new ConsumableBag(currentState.bag().snapshotRemains());
                nextBagWithItem.use(item);
                int itemNextStamina = item.apply(nextStamina);

                List<Tick> newPathWithItem = new ArrayList<>(currentState.path());
                newPathWithItem.add(new Tick(place, action, Math.abs(delta), item));

                SearchState nextStateWithItem = new SearchState(currentState.tick() + 1, itemNextStamina, newPathWithItem, nextBagWithItem, nextActionCount);
                solveRecursive(nextStateWithItem, context, uniqueCombinations, visitedStates);
            }
        }
    }
}
