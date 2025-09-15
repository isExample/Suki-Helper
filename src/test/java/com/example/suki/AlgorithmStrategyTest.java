package com.example.suki;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.algorithm.BfsReachStrategy;
import com.example.suki.domain.simulation.algorithm.DfsFinishStrategy;
import com.example.suki.domain.simulation.goal.FinishAtGoal;
import com.example.suki.domain.simulation.goal.FinishWithinGoal;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlgorithmStrategyTest {
    private static Stream<Arguments> strategyAndGoalProvider() {
        DaySchedule weekdaySchedule = (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second);
        DaySchedule weekendSchedule = (tick, second) -> second;

        // [테스트 이름, 전략, 목표, 요일, 스케줄] 조합을 생성
        return Stream.of(
                // --- BFS 시나리오 ---
                Arguments.of("BFS 평일 - Reach Goal", new BfsReachStrategy(), new ReachGoal(84), DayCategory.WEEKDAY_OTHER, weekdaySchedule),
                Arguments.of("BFS 주말 - Reach Goal", new BfsReachStrategy(), new ReachGoal(84), DayCategory.WEEKEND, weekendSchedule),
                Arguments.of("BFS 평일 - FinishAt Goal", new BfsReachStrategy(), new FinishAtGoal(84), DayCategory.WEEKDAY_OTHER, weekdaySchedule),
                Arguments.of("BFS 주말 - FinishAt Goal", new BfsReachStrategy(), new FinishAtGoal(84), DayCategory.WEEKEND, weekendSchedule),
                Arguments.of("BFS 평일 - FinishWithin Goal", new BfsReachStrategy(), new FinishWithinGoal(80, 86), DayCategory.WEEKDAY_OTHER, weekdaySchedule),
                Arguments.of("BFS 주말 - FinishWithin Goal", new BfsReachStrategy(), new FinishWithinGoal(80, 86), DayCategory.WEEKEND, weekendSchedule),

                // --- DFS 시나리오 ---
                Arguments.of("DFS 평일 - Reach Goal", new DfsFinishStrategy(), new ReachGoal(84), DayCategory.WEEKDAY_OTHER, weekdaySchedule),
                Arguments.of("DFS 주말 - Reach Goal", new DfsFinishStrategy(), new ReachGoal(84), DayCategory.WEEKEND, weekendSchedule),
                Arguments.of("DFS 평일 - FinishAt Goal", new DfsFinishStrategy(), new FinishAtGoal(84), DayCategory.WEEKDAY_OTHER, weekdaySchedule),
                Arguments.of("DFS 주말 - FinishAt Goal", new DfsFinishStrategy(), new FinishAtGoal(84), DayCategory.WEEKEND, weekendSchedule),
                Arguments.of("DFS 평일 - FinishWithin Goal", new DfsFinishStrategy(), new FinishWithinGoal(80, 86), DayCategory.WEEKDAY_OTHER, weekdaySchedule),
                Arguments.of("DFS 주말 - FinishWithin Goal", new DfsFinishStrategy(), new FinishWithinGoal(80, 86), DayCategory.WEEKEND, weekendSchedule)
        );
    }

    @ParameterizedTest(name = "[{0}] 테스트")
    @MethodSource("strategyAndGoalProvider")
    void 기본_UserState는_주어진_목표를_만족하는_모든_유효한_조합을_반환한다(String testName, AlgorithmStrategy strategy, Goal goal, DayCategory day, DaySchedule schedule) {
        UserState userState = new UserState(day);
        ConsumableBag bag = new ConsumableBag(Map.of());

        List<List<Tick>> allSolutions = findAllSolutions(strategy, userState, goal, schedule, bag);

        assertThat(allSolutions).isNotEmpty();
        for (List<Tick> combination : allSolutions) {
            if (goal instanceof FinishAtGoal || goal instanceof FinishWithinGoal) {
                assertThat(combination.size()).isEqualTo(AlgorithmStrategy.MAX_TICKS);
            }
            verifyCombination(combination, goal);
        }

        System.out.printf("[%s] 테스트 성공: 총 %d개의 유효한 조합을 검증했습니다.%n", testName, allSolutions.size());
    }

    private List<List<Tick>> findAllSolutions(AlgorithmStrategy strategy, UserState userState, Goal goal, DaySchedule schedule, ConsumableBag bag) {
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
    private void verifyCombination(List<Tick> combination, Goal goal) {
        int currentStamina = 100; // 초기 체력

        for (Tick tick : combination) {
            int delta = tick.place().getActions().get(tick.action());
            currentStamina += delta;

            if (tick.item() != null) {
                currentStamina = tick.item().apply(currentStamina);
            }
            currentStamina = Math.min(100, currentStamina);
        }
        assertTrue(goal.isSuccess(combination.size(), currentStamina));
    }
}
