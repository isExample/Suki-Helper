package com.example.suki;

import lombok.Getter;

import java.util.Map;

@Getter
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
    )),
    HOME("집", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 13    // 잠자기 특화 장소 보너스 + 3
    )),
    PARK("공원", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    )),
    CAFE("카페", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    )),
    LIBRARY("도서관", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    )),

    /*
     * 조건부 장소
     */
    GOLD_MINE("금광", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -24,  // 알바 특화 보너스(+5)
            ActionCategory.SLEEP, 10
    )),
    ART_GALLERY("미술관", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 12    // 잠자기 특화 장소 보너스 + 2
    )),
    GYM("헬스장", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -29,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10
    )),
    PC_ROOM("피시방", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.PLAY_GAME, -5
    )),
    FOOTBALL_PITCH("축구연습장", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.FOOTBALL, -14
    )),
    WORKSHOP("작업실", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.DRAWING, -11
    )),
    PRACTICE_ROOM("연습실", Map.of(
            ActionCategory.STUDY, -16,
            ActionCategory.EXERCISE, -24,
            ActionCategory.PART_TIME, -29,
            ActionCategory.SLEEP, 10,
            ActionCategory.TRAINING, -4
    ));

    private final String description;
    private final Map<ActionCategory, Integer> actions;

    PlaceCategory(String description, Map<ActionCategory, Integer> actions) {
        this.description = description;
        this.actions = actions;
    }
}
