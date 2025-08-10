package com.example.suki;

import lombok.Getter;

import java.util.Map;

@Getter
public enum PlaceCategory {
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
    ));

    private final String description;
    private final Map<ActionCategory, Integer> actions;

    PlaceCategory(String description, Map<ActionCategory, Integer> actions) {
        this.description = description;
        this.actions = actions;
    }
}
