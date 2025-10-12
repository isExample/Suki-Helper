package com.example.suki.performance;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.algorithm.BfsReachStrategy;
import com.example.suki.domain.simulation.algorithm.DfsFinishStrategy;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlgorithmPerformanceTest {
    private final int REPETITIONS = 100;
    private Map<String, Long> totalExecutionTimes;
    private Map<String, Long> totalVisitedNodes;

    @BeforeAll
    void setup() {
        totalExecutionTimes = new HashMap<>();
        totalVisitedNodes = new HashMap<>();
    }

    @AfterAll
    void reportAverageTimes() {
        System.out.println("\n==================== 성능 테스트 결과 ====================");

        totalExecutionTimes.forEach((strategyName, totalTime) -> {
            long totalNodes = totalVisitedNodes.getOrDefault(strategyName, 0L);
            System.out.printf("[%s] 평균 실행 시간: %.3f ms | 평균 탐색 노드: %,d 개%n",
                    strategyName,
                    (double) totalTime / REPETITIONS / 1_000_000,
                    totalNodes / REPETITIONS);
        });

        System.out.println("========================================================");
    }

    private static Stream<Arguments> strategyProvider() {
        return Stream.of(
                Arguments.of("BFS", new BfsReachStrategy()),
                Arguments.of("DFS", new DfsFinishStrategy())
                // 여기에 다른 전략 알고리즘 추가
        );
    }

    @ParameterizedTest(name = "[{0}] 전략 성능 측정")
    @MethodSource("strategyProvider")
    void measurePerformance(String strategyName, AlgorithmStrategy strategy) {
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);
        Goal goal = new ReachGoal(84); // 목표 Goal을 여기서 수정
        int currentTick = 0;
        int currentStamina = 100;
        DaySchedule schedule = (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second);
        ConsumableBag bag = new ConsumableBag(Map.of());

        long cumulativeTime = 0;
        long cumulativeNodes = 0;

        for (int i = 0; i < REPETITIONS; i++) {
            long startTime = System.nanoTime();
            int visitedNodeCount = findAllSolutions(strategy, userState, goal, currentTick, currentStamina, schedule, bag);
            long endTime = System.nanoTime();

            cumulativeTime += (endTime - startTime);
            cumulativeNodes += visitedNodeCount;
        }

        totalExecutionTimes.put(strategyName, cumulativeTime);
        totalVisitedNodes.put(strategyName, cumulativeNodes);
    }

    private int findAllSolutions(AlgorithmStrategy strategy, UserState userState, Goal goal, int currentTick, int currentStamina, DaySchedule schedule, ConsumableBag bag) {
        List<List<Tick>> solutions = new ArrayList<>();
        int totalVisitedCount = 0;
        for (PlaceCategory secondPlace : userState.getPlaces().keySet()) {
            SimulationContext context = new SimulationContext(
                    userState,
                    goal,
                    currentTick,
                    currentStamina,
                    bag,
                    secondPlace,
                    schedule,
                    solutions
            );
            totalVisitedCount += strategy.solve(context);
        }
        return totalVisitedCount;
    }
}
