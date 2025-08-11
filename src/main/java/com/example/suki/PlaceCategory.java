package com.example.suki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum PlaceCategory {
    /*
     * 기본 장소
     */
    SCHOOL("학교", Map.of(
            ActionCategory.ATTEND_CLASS, -9,
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    ), Set.of(ActionCategory.EXERCISE, ActionCategory.PART_TIME), true),
    HOME("집", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 13    // 잠자기 특화 장소 보너스 + 3
    ), Set.of(ActionCategory.SLEEP), true),
    PARK("공원", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    ), Set.of(ActionCategory.EXERCISE), true),
    CAFE("카페", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    ), Set.of(ActionCategory.PART_TIME), true),
    LIBRARY("도서관", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    ), Set.of(ActionCategory.STUDY), true),

    /*
     * 조건부 장소
     */
    GOLD_MINE("금광", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -24,  // 알바 특화 보너스(+5)
            ActionCategory.SLEEP, 10
    ), Set.of(ActionCategory.PART_TIME), false),
    ART_GALLERY("미술관", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 12    // 잠자기 특화 장소 보너스 + 2
    ), Set.of(ActionCategory.SLEEP), false),
    GYM("헬스장", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -29,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    ), Set.of(ActionCategory.EXERCISE), false),
    PC_ROOM("피시방", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.PLAY_GAME, -5
    ), Set.of(ActionCategory.PLAY_GAME), false),
    FOOTBALL_PITCH("축구연습장", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.FOOTBALL, -14
    ), Set.of(ActionCategory.FOOTBALL), false),
    WORKSHOP("작업실", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.DRAWING, -11
    ), Set.of(ActionCategory.DRAWING), false),
    PRACTICE_ROOM("연습실", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.TRAINING, -4
    ), Set.of(ActionCategory.TRAINING), false);

    private final String description;
    private final Map<ActionCategory, Integer> actions;
    private final Set<ActionCategory> specializations;
    private final boolean isDefault;

    public boolean isSpecializedFor(ActionCategory category) {
        return this.specializations.contains(category);
    }
}
