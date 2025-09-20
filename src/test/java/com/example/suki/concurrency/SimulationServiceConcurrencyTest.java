package com.example.suki.concurrency;

import com.example.suki.api.dto.SimulationRangeRequest;
import com.example.suki.api.dto.SimulationRangeResponse;
import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.api.dto.SimulationResponse;
import com.example.suki.application.SimulationService;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SimulationServiceConcurrencyTest {
    @Autowired
    private SimulationService simulationService;

    @Test
    @DisplayName("여러 스레드가 동시에 simulateReach를 호출해도 항상 동일한 결과를 반환해야 한다")
    void simulateReach_concurrency_test() throws InterruptedException {
        int threadCount = 50; // 동시에 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // 모든 스레드가 사용할 동일 요청 객체
        SimulationRequest request = new SimulationRequest(
                84,
                0,
                DayCategory.WEEKDAY_OTHER,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                Map.of()
        );

        // 결과 조합 확보
        List<List<Tick>> expectedCombinations = simulationService.simulateReach(request).combinations();
        assertThat(expectedCombinations).isNotEmpty(); // 최소 하나 이상의 결과가 나온다고 가정

        // 여러 스레드에서 동시에 서비스 호출
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    SimulationResponse response = simulationService.simulateReach(request);
                    List<List<Tick>> actualCombinations = response.combinations();

                    // 반환된 결과 조합이 기대값과 일치하는지 검증
                    boolean isIdentical = expectedCombinations.size() == actualCombinations.size() &&
                            new HashSet<>(expectedCombinations).equals(new HashSet<>(actualCombinations));

                    if (isIdentical) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 작업을 완료할 때까지 대기
        latch.await();
        executorService.shutdown();

        System.out.println("\n--- 동시성 테스트 결과 ---");
        System.out.printf("총 요청 수: %d\n", threadCount);
        System.out.printf("성공: %d\n", successCount.get());
        System.out.printf("실패: %d\n", failureCount.get());
        System.out.println("----------------------\n");

        assertThat(failureCount.get())
                .as("동시성 이슈 발생.")
                .isZero();
    }

    @Test
    @DisplayName("여러 스레드가 동시에 simulateFinishAt을 호출해도 항상 동일한 결과를 반환해야 한다")
    void simulateFinishAt_concurrency_test() throws InterruptedException {
        int threadCount = 50; // 동시에 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // 모든 스레드가 사용할 동일 요청 객체
        SimulationRequest request = new SimulationRequest(
                84,
                0,
                DayCategory.WEEKDAY_OTHER,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                Map.of()
        );

        // 결과 조합 확보
        List<List<Tick>> expectedCombinations = simulationService.simulateFinishAt(request).combinations();
        assertThat(expectedCombinations).isNotEmpty(); // 최소 하나 이상의 결과가 나온다고 가정

        // 여러 스레드에서 동시에 서비스 호출
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    SimulationResponse response = simulationService.simulateFinishAt(request);
                    List<List<Tick>> actualCombinations = response.combinations();

                    // 반환된 결과 조합이 기대값과 일치하는지 검증
                    boolean isIdentical = expectedCombinations.size() == actualCombinations.size() &&
                            new HashSet<>(expectedCombinations).equals(new HashSet<>(actualCombinations));

                    if (isIdentical) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 작업을 완료할 때까지 대기
        latch.await();
        executorService.shutdown();

        System.out.println("\n--- 동시성 테스트 결과 ---");
        System.out.printf("총 요청 수: %d\n", threadCount);
        System.out.printf("성공: %d\n", successCount.get());
        System.out.printf("실패: %d\n", failureCount.get());
        System.out.println("----------------------\n");

        assertThat(failureCount.get())
                .as("동시성 이슈 발생.")
                .isZero();
    }

    @Test
    @DisplayName("여러 스레드가 동시에 simulateFinishWithin을 호출해도 항상 동일한 결과를 반환해야 한다")
    void simulateFinishWithin_concurrency_test() throws InterruptedException {
        int threadCount = 50; // 동시에 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // 모든 스레드가 사용할 동일 요청 객체
        SimulationRangeRequest request = new SimulationRangeRequest(
                84,
                89,
                0,
                DayCategory.WEEKEND,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                Map.of()
        );

        // 결과 조합 확보
        List<List<Tick>> expectedCombinations = simulationService.simulateFinishWithin(request).combinations();
        assertThat(expectedCombinations).isNotEmpty(); // 최소 하나 이상의 결과가 나온다고 가정

        // 여러 스레드에서 동시에 서비스 호출
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    SimulationRangeResponse response = simulationService.simulateFinishWithin(request);
                    List<List<Tick>> actualCombinations = response.combinations();

                    // 반환된 결과 조합이 기대값과 일치하는지 검증
                    boolean isIdentical = expectedCombinations.size() == actualCombinations.size() &&
                            new HashSet<>(expectedCombinations).equals(new HashSet<>(actualCombinations));

                    if (isIdentical) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 작업을 완료할 때까지 대기
        latch.await();
        executorService.shutdown();

        System.out.println("\n--- 동시성 테스트 결과 ---");
        System.out.printf("총 요청 수: %d\n", threadCount);
        System.out.printf("성공: %d\n", successCount.get());
        System.out.printf("실패: %d\n", failureCount.get());
        System.out.println("----------------------\n");

        assertThat(failureCount.get())
                .as("동시성 이슈 발생.")
                .isZero();
    }
}
