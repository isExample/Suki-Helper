package com.example.suki;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.algorithm.BfsReachStrategy;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class BfsReachStrategyTest {
    private final BfsReachStrategy strategy = new BfsReachStrategy();

    @Test
    void 평일에_기본_UserState는_목표체력_84에_도달하는_유효한_조합을_반환한다() {
        DayCategory day = DayCategory.WEEKDAY_OTHER;
        UserState userState = new UserState(day);

        int targetStamina = 84;
        Goal goal = new ReachGoal(targetStamina);
        ConsumableBag bag = new ConsumableBag(Map.of());

        // size()가 항상 0을 반환하는 가짜 List 생성 - 모든 조합 수 반환 목적
        final List<List<Tick>> actualSolutions = new ArrayList<>();
        List<List<Tick>> lyingList = new ArrayList<>() {
            @Override
            public int size() {
                return 0;   // MAX_SOLUTIONS 체크를 우회하기 위해 항상 0을 반환
            }

            @Override
            public boolean add(List<Tick> ticks) {
                return actualSolutions.add(ticks);  // 데이터는 실제 리스트에 저장
            }
        };

        for (PlaceCategory secondPlace : userState.getPlaces().keySet()) {
            SimulationContext context = new SimulationContext(
                    userState,
                    goal,
                    secondPlace,
                    (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second),
                    bag,
                    lyingList
            );
            strategy.solve(context);
        }

        assertThat(actualSolutions).isNotEmpty();
        for (List<Tick> combination : actualSolutions) {
            verifyCombination(combination, targetStamina);
        }

        System.out.printf("테스트 성공: 총 %d개의 유효한 조합을 검증했습니다.%n", actualSolutions.size());
    }

    // 결과 조합의 유효성을 검증하는 헬퍼 메서드
    private void verifyCombination(List<Tick> combination, int targetStamina) {
        int currentStamina = 100; // 초기 체력

        for (Tick tick : combination) {
            int delta = tick.place().getActions().get(tick.action());
            currentStamina += delta;

            if (tick.item() != null) {
                currentStamina = tick.item().apply(currentStamina);
            }
            currentStamina = Math.min(100, currentStamina);
        }
        assertThat(currentStamina).isEqualTo(targetStamina);
    }
}
