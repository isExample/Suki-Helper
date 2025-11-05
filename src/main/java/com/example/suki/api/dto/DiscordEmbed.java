package com.example.suki.api.dto;

import java.util.List;

public record DiscordEmbed(
        String title,
        String description,
        List<DiscordEmbedField> fields
) {
}
