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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgorithmPerformanceTest {
    private static long totalBfsTime = 0;
    private static long totalDfsTime = 0;

    @AfterAll
    static void reportAverageTimes() {
        int repetitions = 100;
        System.out.println("\n==================== 성능 테스트 결과 ====================");
        if (totalBfsTime > 0) {
            System.out.printf("[ReachGoal] BFS 평균 실행 시간: %.3f ms%n", (double) totalBfsTime / repetitions / 1_000_000);
        }
        if (totalDfsTime > 0) {
            System.out.printf("[ReachGoal] DFS 평균 실행 시간: %.3f ms%n", (double) totalDfsTime / repetitions / 1_000_000);
        }
        System.out.println("======================================================");
    }

    @RepeatedTest(value = 100, name = "[BFS] {currentRepetition}/{totalRepetitions} 회차 실행 중...")
    @DisplayName("[ReachGoal] BFS 전략 성능 측정")
    void measureBfsPerformance() {
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);
        Goal goal = new ReachGoal(84); // 테스트할 기능 선택
        DaySchedule schedule = (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second);
        ConsumableBag bag = new ConsumableBag(Map.of());
        BfsReachStrategy bfs = new BfsReachStrategy();

        long startTime = System.nanoTime();
        findAllSolutions(bfs, userState, goal, schedule, bag);
        long endTime = System.nanoTime();

        totalBfsTime += (endTime - startTime);
    }

    @RepeatedTest(value = 100, name = "[DFS] {currentRepetition}/{totalRepetitions} 회차 실행 중...")
    @DisplayName("[ReachGoal] DFS 전략 성능 측정")
    void measureDfsPerformance() {
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);
        Goal goal = new ReachGoal(84); // 테스트할 기능 선택
        DaySchedule schedule = (tick, second) -> (tick < 6 ? PlaceCategory.SCHOOL : second);
        ConsumableBag bag = new ConsumableBag(Map.of());
        DfsFinishStrategy dfs = new DfsFinishStrategy();

        long startTime = System.nanoTime();
        findAllSolutions(dfs, userState, goal, schedule, bag);
        long endTime = System.nanoTime();

        totalDfsTime += (endTime - startTime);
    }

    private List<List<Tick>> findAllSolutions(AlgorithmStrategy strategy, UserState userState, Goal goal, DaySchedule schedule, ConsumableBag bag) {
        List<List<Tick>> solutions = new ArrayList<>();
        for (PlaceCategory secondPlace : userState.getPlaces().keySet()) {
            SimulationContext context = new SimulationContext(
                    userState,
                    goal,
                    secondPlace,
                    schedule,
                    bag,
                    solutions
            );
            strategy.solve(context);
        }
        return solutions;
    }
}
