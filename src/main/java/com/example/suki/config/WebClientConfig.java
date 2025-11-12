package com.example.suki.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // Netty의 HttpClient를 기반으로 타임아웃을 정교하게 설정합니다.
        HttpClient httpClient = HttpClient.create()
                // 1. Connection Timeout: 서버와 연결을 맺는 데까지 대기할 시간 (5초)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 2. Response Timeout: 요청을 보낸 후 응답 전체를 받기까지 대기할 총 시간 (10초)
                .responseTimeout(Duration.ofSeconds(10))
                // 3. Read/Write Timeout: 연결된 후, 각 데이터 조각을 읽고 쓰는 데 대기할 시간 (10초)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)));

        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
