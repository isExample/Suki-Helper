package com.example.suki.performance;

import com.example.suki.domain.User.UserState;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.DaySchedule;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.algorithm.BfsReachStrategy;
import com.example.suki.domain.simulation.algorithm.DfsFinishStrategy;
import com.example.suki.domain.simulation.goal.FinishAtGoal;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgorithmPerformanceTest {
    private static long totalBfsTime = 0;
    private static long totalDfsTime = 0;
    private static long totalBfsVisitedNodes = 0;
    private static long totalDfsVisitedNodes = 0;

    private record PerformanceResult(int visitedNodeCount) {}

    @AfterAll
    static void reportAverageTimes() {
        int repetitions = 100;
        System.out.println("\n==================== 성능 테스트 결과 ====================");
        if (totalBfsTime > 0) {
            System.out.printf("[ReachGoal] BFS 평균 실행 시간: %.3f ms | 평균 탐색 노드: %,d 개%n", (double) totalBfsTime / repetitions / 1_000_000, totalBfsVisitedNodes / repetitions);
        }
        if (totalDfsTime > 0) {
            System.out.printf("[ReachGoal] DFS 평균 실행 시간: %.3f ms | 평균 탐색 노드: %,d 개%n", (double) totalDfsTime / repetitions / 1_000_000, totalDfsVisitedNodes / repetitions);
        }
        System.out.println("======================================================");
    }

    @RepeatedTest(value = 100, name = "[BFS] {currentRepetition}/{totalRepetitions} 회차 실행 중...")
    @DisplayName("[ReachGoal] BFS 전략 성능 측정")
    void measureBfsPerformance() {
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);
        Goal goal = new FinishAtGoal(84); // 테스트할 기능 선택
        DaySchedule schedule = (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second);
        ConsumableBag bag = new ConsumableBag(Map.of());
        BfsReachStrategy bfs = new BfsReachStrategy();

        long startTime = System.nanoTime();
        PerformanceResult result = findAllSolutions(bfs, userState, goal, schedule, bag);
        long endTime = System.nanoTime();

        totalBfsTime += (endTime - startTime);
        totalBfsVisitedNodes += result.visitedNodeCount();
    }

    @RepeatedTest(value = 100, name = "[DFS] {currentRepetition}/{totalRepetitions} 회차 실행 중...")
    @DisplayName("[ReachGoal] DFS 전략 성능 측정")
    void measureDfsPerformance() {
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);
        Goal goal = new FinishAtGoal(84); // 테스트할 기능 선택
        DaySchedule schedule = (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second);
        ConsumableBag bag = new ConsumableBag(Map.of());
        DfsFinishStrategy dfs = new DfsFinishStrategy();

        long startTime = System.nanoTime();
        PerformanceResult result = findAllSolutions(dfs, userState, goal, schedule, bag);
        long endTime = System.nanoTime();

        totalDfsTime += (endTime - startTime);
        totalDfsVisitedNodes += result.visitedNodeCount();
    }

    private PerformanceResult findAllSolutions(AlgorithmStrategy strategy, UserState userState, Goal goal, DaySchedule schedule, ConsumableBag bag) {
        List<List<Tick>> solutions = new ArrayList<>();
        int totalVisitedCount = 0;
        for (PlaceCategory secondPlace : userState.getPlaces().keySet()) {
            SimulationContext context = new SimulationContext(
                    userState,
                    goal,
                    secondPlace,
                    schedule,
                    bag,
                    solutions
            );
            totalVisitedCount += strategy.solve(context);
        }
        return new PerformanceResult(totalVisitedCount);
    }
}
