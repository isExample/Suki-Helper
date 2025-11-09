package com.example.suki.performance;

import com.example.suki.api.dto.SupportRequest;
import com.example.suki.application.NotificationService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@DisplayName("NotificationService 성능 테스트")
class NotificationPerformanceTest {

    private static MockWebServer mockWebServer;
    private NotificationService notificationService;
    private final Random random = new Random();

    // 1. 각 시나리오별 실행 시간을 저장할 두 개의 독립된 리스트 생성
    private static final List<Long> successTimes = Collections.synchronizedList(new ArrayList<>());
    private static final List<Long> failureOrDelayTimes = Collections.synchronizedList(new ArrayList<>());

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
        printAllStatistics(); // 두 시나리오의 통계를 함께 출력
    }

    @BeforeEach
    void initialize() {
        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().build();
        notificationService = new NotificationService(webClient, baseUrl);
    }

    // --- 테스트 시나리오 1: 즉시 성공 ---
    @DisplayName("외부 API가 즉시 성공할 때 서비스 실행 시간 측정")
    @RepeatedTest(value = 100, name = "[성공 시나리오] {currentRepetition}/{totalRepetitions}")
    void testPerformance_onApiSuccess() {
        // given: 외부 API가 항상 즉시 성공(204)하도록 설정
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        SupportRequest request = new SupportRequest("성능 테스트", "즉시 성공 시나리오");
        StopWatch stopWatch = new StopWatch();

        // when
        stopWatch.start();
        notificationService.sendDiscordNotification(request);
        stopWatch.stop();

        // then
        successTimes.add(stopWatch.getNanoTime());
    }

    // --- 테스트 시나리오 2: 지연 또는 실패 ---
    @DisplayName("외부 API가 지연/실패할 때 서비스 실행 시간 측정")
    @RepeatedTest(value = 100, name = "[지연/실패 시나리오] {currentRepetition}/{totalRepetitions}")
    void testPerformance_onApiFailureOrDelay() {
        // given: 외부 API가 지연 후 성공 또는 실패를 무작위로 응답하도록 설정
        setupRandomMockResponseForFailure();

        SupportRequest request = new SupportRequest("성능 테스트", "지연/실패 시나리오");
        StopWatch stopWatch = new StopWatch();

        // when
        stopWatch.start();
        notificationService.sendDiscordNotification(request);
        stopWatch.stop();

        // then
        failureOrDelayTimes.add(stopWatch.getNanoTime());
    }

    private void setupRandomMockResponseForFailure() {
        if (random.nextBoolean()) {
            // 시나리오 1: 1초 지연 후 성공
            mockWebServer.enqueue(new MockResponse().setResponseCode(204).setBodyDelay(1, TimeUnit.SECONDS));
        } else {
            // 시나리오 2: 1초 지연 후 실패
            mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBodyDelay(1, TimeUnit.SECONDS));
        }
    }

    private static void printAllStatistics() {
        printStatistics("API 즉시 성공", successTimes);
        printStatistics("API 지연/실패", failureOrDelayTimes);
    }

    private static void printStatistics(String testName, List<Long> executionTimes) {
        if (executionTimes.isEmpty()) return;

        double average = executionTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        long min = executionTimes.stream().mapToLong(Long::longValue).min().orElse(0);
        long max = executionTimes.stream().mapToLong(Long::longValue).max().orElse(0);

        System.out.printf("\n--- Performance Test Results (%s) ---\n", testName);
        System.out.printf("Total Runs: %d\n", executionTimes.size());
        System.out.printf("Average Execution Time: %.3f ms\n", average / 1_000_000.0);
        System.out.printf("Min Execution Time:     %.3f ms\n", min / 1_000_000.0);
        System.out.printf("Max Execution Time:     %.3f ms\n", max / 1_000_000.0);
        System.out.println("----------------------------------------------------");
    }
}
