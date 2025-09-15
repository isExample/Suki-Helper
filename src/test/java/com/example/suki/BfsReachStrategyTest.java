package com.example.suki;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.algorithm.BfsReachStrategy;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class BfsReachStrategyTest {
    private final BfsReachStrategy strategy = new BfsReachStrategy();

    private static Stream<Arguments> dayAndScheduleProvider() {
        return Stream.of(
                Arguments.of(
                        "평일",
                        DayCategory.WEEKDAY_OTHER,
                        (DaySchedule) (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second)
                ),
                Arguments.of(
                        "주말",
                        DayCategory.WEEKEND,
                        (DaySchedule) (tick, second) -> second
                )
        );
    }

    @ParameterizedTest(name = "[{0}] 테스트")
    @MethodSource("dayAndScheduleProvider")
    void 기본_UserState는_목표체력에_도달하는_유효한_조합을_반환한다(String testName, DayCategory day, DaySchedule schedule) {
        UserState userState = new UserState(day);
        int targetStamina = 84;
        Goal goal = new ReachGoal(targetStamina);
        ConsumableBag bag = new ConsumableBag(Map.of());

        List<List<Tick>> allSolutions = findAllSolutions(userState, goal, schedule, bag);

        assertThat(allSolutions).isNotEmpty();
        for (List<Tick> combination : allSolutions) {
            verifyCombination(combination, targetStamina);
        }

        System.out.printf("[%s] 테스트 성공: 총 %d개의 유효한 조합을 검증했습니다.%n", testName, allSolutions.size());
    }

    private List<List<Tick>> findAllSolutions(UserState userState, Goal goal, DaySchedule schedule, ConsumableBag bag) {
        final List<List<Tick>> actualSolutions = new ArrayList<>();
        List<List<Tick>> lyingList = createLyingList(actualSolutions);

        for (PlaceCategory secondPlace : userState.getPlaces().keySet()) {
            SimulationContext context = new SimulationContext(
                    userState,
                    goal,
                    secondPlace,
                    schedule,
                    bag,
                    lyingList
            );
            strategy.solve(context);
        }
        return actualSolutions;
    }

    private List<List<Tick>> createLyingList(List<List<Tick>> actualList) {
        return new ArrayList<>() {
            @Override
            public int size() {
                return 0; // MAX_SOLUTIONS 체크를 우회하기 위해 항상 0을 반환
            }

            @Override
            public boolean add(List<Tick> ticks) {
                return actualList.add(ticks); // 데이터 저장하는 실제 리스트
            }
        };
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
