package com.example.suki.api.dto;

import java.util.List;

public record DiscordWebhookPayload(
        String username,
        List<DiscordEmbed> embeds
) {
}
