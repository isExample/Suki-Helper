package com.example.suki.domain.simulation;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.Tick;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BfsAlgorithm implements AlgorithmStrategy {
    private static final int MAX_STAMINA = 100;
    private static final int MIN_STAMINA = 0;
    private static final int MAX_TICKS = 14;
    private static final int MAX_SOLUTIONS = 10;

    private record SearchState(int tick, int stamina, List<Tick> path, ConsumableBag bag) {}

    @Override
    public boolean supports(AlgorithmType algorithmType) {
        return algorithmType == AlgorithmType.BFS;
    }

    @Override
    public void solve(UserState userState, int currentTick, int currentStamina, Goal goal,
                      PlaceCategory secondPlace, DaySchedule schedule, List<Tick> path, ConsumableBag consumableBag, List<List<Tick>> solutions) {
        Queue<SearchState> queue = new LinkedList<>();
        SearchState initialState = new SearchState(currentTick, currentStamina, path, consumableBag);
        queue.add(initialState);

        while (!queue.isEmpty()) {
            SearchState currentState = queue.poll();
            int tick = currentState.tick();
            int stamina = currentState.stamina();
            ConsumableBag bag = currentState.bag();

            // 성공 조건 확인
            if (goal.isSuccess(tick, stamina)) {
                solutions.add(List.copyOf(currentState.path()));
                if (solutions.size() >= MAX_SOLUTIONS) {
                    return; // 최대 조합 개수: 즉시 종료
                }
            }

            // 종료 조건을 확인: 최대 틱 수 도달
            if (goal.isTerminal(tick, stamina, MAX_TICKS)) {
                continue;
            }

            PlaceCategory currentPlace = schedule.placeAt(tick, secondPlace);
            Map<ActionCategory, Integer> actions = userState.getPlaces().get(currentPlace).getActions();
            for (Map.Entry<ActionCategory, Integer> entry : actions.entrySet()) {
                ActionCategory action = entry.getKey();
                int delta = entry.getValue();

                // 체력이 100이면서 회복 행동이면 해당 경로 종료
                if(stamina == MAX_STAMINA && delta >= 0){
                    continue;
                }

                int nextStamina = Math.min(MAX_STAMINA, stamina + delta);

                // 체력이 0 이하가 되면 해당 경로 종료
                if (nextStamina <= MIN_STAMINA) {
                    continue;
                }

                // 소비성 아이템을 사용하지 않는 경우
                List<Tick> newPathNoItem = new ArrayList<>(currentState.path());
                newPathNoItem.add(new Tick(currentPlace, action, Math.abs(delta), null));

                SearchState nextStateNoItem = new SearchState(tick + 1, nextStamina, newPathNoItem, bag);
                queue.add(nextStateNoItem);
            }
        }
    }
}
