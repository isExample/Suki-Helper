package com.example.suki.application;

import com.example.suki.api.dto.DiscordEmbed;
import com.example.suki.api.dto.DiscordEmbedField;
import com.example.suki.api.dto.DiscordWebhookPayload;
import com.example.suki.api.dto.SupportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class NotificationService {
    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendDiscordNotification(SupportRequest request) {
        if (discordWebhookUrl == null || discordWebhookUrl.isBlank()) {
            log.warn("Discord 웹훅 URL이 설정되어 있지 않습니다. 피드백 전송을 생략합니다.");
            return;
        }

        try{
            DiscordEmbedField typeField = new DiscordEmbedField("유형", request.type(), true);
            DiscordEmbedField messageField = new DiscordEmbedField("내용", request.message(), false);

            DiscordEmbed embed = new DiscordEmbed(
                    "🔔 새로운 피드백 도착!",
                    "사용자로부터 새로운 피드백이 제출되었습니다.",
                    List.of(typeField, messageField)
            );

            DiscordWebhookPayload payload = new DiscordWebhookPayload(
                    "Suki Helper 피드백 봇",
                    List.of(embed)
            );

            restTemplate.postForObject(discordWebhookUrl, payload, String.class);
            log.info("Discord로 피드백이 성공적으로 전송되었습니다.");

        } catch (Exception e){
            log.error("Discord 웹훅 피드백 전송에 실패했습니다.", e);
        }
    }

}
