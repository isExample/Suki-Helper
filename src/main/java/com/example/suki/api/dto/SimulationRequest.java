package com.example.suki.api.dto;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.item.ItemCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.trait.TraitCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "체력 달성 조합 요청")
public record SimulationRequest(
        @Schema(description="목표 체력")
        int targetStamina,
        @Schema(description="운동 레벨", defaultValue = "0")
        int fitnessLevel,
        @Schema(description="요일 정보", defaultValue = "WEEKDAY_OTHER")
        DayCategory day,
        @Schema(description="비활성화된 기본 장소 목록")
        List<PlaceCategory> inactiveList,
        @Schema(description="활성화된 조건부 장소 목록")
        List<PlaceCategory> activeList,
        @Schema(description="대학교 뱃지 목록")
        List<BadgeCategory> badgeList,
        @Schema(description="특성 목록")
        List<TraitCategory> traitList,
        @Schema(description="아이템 목록")
        List<ItemCategory> itemList
) {
}
