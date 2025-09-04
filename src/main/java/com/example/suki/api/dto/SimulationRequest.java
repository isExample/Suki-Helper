package com.example.suki.api.dto;

import com.example.suki.api.validation.CoffeeMaxOne;
import com.example.suki.api.validation.CoffeeMutuallyExclusive;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.item.ConsumableItemCategory;
import com.example.suki.domain.item.PermanentItemCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.trait.TraitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

@Schema(description = "체력 달성 조합 요청")
public record SimulationRequest(
        @Schema(description="목표 체력")
        @Min(1) @Max(99)
        int targetStamina,

        @Schema(description="운동 레벨", defaultValue = "0")
        @Min(0) @Max(10)
        int fitnessLevel,

        @Schema(description="요일 정보", defaultValue = "WEEKDAY_OTHER")
        @NotNull
        DayCategory day,

        @Schema(description="비활성화된 기본 장소 목록")
        @NotNull
        List<PlaceCategory> inactiveList,

        @Schema(description="활성화된 조건부 장소 목록")
        @NotNull
        List<PlaceCategory> activeList,

        @Schema(description="대학교 뱃지 목록")
        @NotNull
        @Size(max = 7)
        List<BadgeCategory> badgeList,

        @Schema(description="특성 목록")
        @NotNull
        @Size(max = 6)
        List<TraitCategory> traitList,

        @Schema(description="영구성 아이템 목록")
        @NotNull
        List<PermanentItemCategory> permanentItemList,

        @Schema(description="소비성 아이템 목록")
        @NotNull
        @CoffeeMaxOne
        @CoffeeMutuallyExclusive
        Map<ConsumableItemCategory, @Min(1) Integer> consumableItemMap
) {
}
