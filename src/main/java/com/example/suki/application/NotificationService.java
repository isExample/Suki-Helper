package com.example.suki.application;

import com.example.suki.api.dto.DiscordEmbed;
import com.example.suki.api.dto.DiscordEmbedField;
import com.example.suki.api.dto.DiscordWebhookPayload;
import com.example.suki.api.dto.SupportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class NotificationService {
    private final WebClient webClient;
    private final String discordWebhookUrl;

    private static final long RETRY_DURATION = 2;
    private static final int MAX_RETRY_COUNT = 3;

    public NotificationService(WebClient webClient, @Value("${discord.webhook.url}") String discordWebhookUrl) {
        this.discordWebhookUrl = discordWebhookUrl;
        this.webClient = webClient;
    }

    public void sendDiscordNotification(SupportRequest request) {
        if (discordWebhookUrl == null || discordWebhookUrl.isBlank()) {
            log.warn("Discord 웹훅 URL이 설정되어 있지 않습니다. 피드백 전송을 생략합니다.");
            return;
        }

        DiscordWebhookPayload payload = createDiscordPayload(request);

        webClient.post()
                .uri(discordWebhookUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(buildRetrySpec())
                .doOnSuccess(v -> log.info("Discord로 피드백이 성공적으로 전송되었습니다."))
                .subscribe(
                        null,
                        error -> log.error(
                                "최종 재시도 후에도 Discord 알림 전송에 실패했습니다. 데이터: {}, 에러: {}",
                                request, // 실패한 데이터
                                error.getMessage()
                        )
                );
    }

    private DiscordWebhookPayload createDiscordPayload(SupportRequest request) {
        DiscordEmbedField typeField = new DiscordEmbedField("유형", request.type(), true);
        DiscordEmbedField messageField = new DiscordEmbedField("내용", request.message(), false);

        DiscordEmbed embed = new DiscordEmbed(
                "🔔 새로운 피드백 도착!",
                "사용자로부터 새로운 피드백이 제출되었습니다.",
                List.of(typeField, messageField)
        );

        return new DiscordWebhookPayload(
                "Suki Helper 피드백 봇",
                List.of(embed)
        );
    }

    /**
     * Retry-After 헤더를 고려하는 동적 재시도 전략 생성
     */
    private Retry buildRetrySpec() {
        return Retry.from(companion -> companion.flatMap(retrySignal -> {
            Throwable failure = retrySignal.failure();
            long attempt = retrySignal.totalRetries() + 1;

            if (!(failure instanceof WebClientResponseException ex)) {
                log.warn("네트워크 에러가 아닌 다른 예외 발생, 재시도하지 않음: {}", failure.getMessage());
                return Mono.error(failure); // 재시도 중단
            }

            // 429 (Too Many Requests) 또는 503 (Service Unavailable) 상태 코드인 경우
            if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS || ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                HttpHeaders headers = ex.getHeaders();
                String retryAfter = headers.getFirst(HttpHeaders.RETRY_AFTER);

                if (retryAfter != null) {
                    Duration delay = parseRetryAfterHeader(retryAfter);
                    log.warn("서버가 Retry-After 헤더로 응답했습니다. ({}초 후 재시도). 시도 횟수: {}", delay.toSeconds(), attempt);
                    return Mono.delay(delay);
                }
            }

            // 일반적인 backoff 전략
            if (attempt <= MAX_RETRY_COUNT) {
                Duration delay = Duration.ofSeconds(RETRY_DURATION);
                log.warn("일반적인 HTTP 에러 발생, {}초 후 재시도. 상태코드: {}, 시도 횟수: {}", delay.toSeconds(), ex.getStatusCode(), attempt);
                return Mono.delay(delay);
            } else {
                log.error("최대 재시도 횟수({}회)를 초과했습니다.", MAX_RETRY_COUNT);
                return Mono.error(failure); // 재시도 중단
            }
        }));
    }

    /**
     * Retry-After 헤더 값을 파싱하여 Duration 객체로 변환
     * 헤더 값은 초 단위: https://discord.com/developers/docs/topics/rate-limits 참조
     */
    private Duration parseRetryAfterHeader(String headerValue) {
        try {
            double seconds = Double.parseDouble(headerValue);
            return Duration.ofMillis((long) (seconds * 1000));
        } catch (NumberFormatException e) {
            log.warn("Retry-After 헤더 파싱에 실패했습니다. 기본값 {}초를 사용합니다. Header: {}", RETRY_DURATION, headerValue);
            return Duration.ofSeconds(RETRY_DURATION);
        }
    }
}
