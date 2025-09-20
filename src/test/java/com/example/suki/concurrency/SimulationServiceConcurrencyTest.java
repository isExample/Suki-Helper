package com.example.suki.concurrency;

import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.api.dto.SimulationResponse;
import com.example.suki.application.SimulationService;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.simulation.model.Tick;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                    assertThat(actualCombinations).containsExactlyElementsOf(expectedCombinations);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 작업을 완료할 때까지 대기
        latch.await();
        executorService.shutdown();

        System.out.printf("%d개의 스레드가 동시에 요청을 전송. %d개의 동일한 결과 조합을 반환.%n", threadCount, expectedCombinations.size());
    }
}
