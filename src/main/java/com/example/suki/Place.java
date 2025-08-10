package com.example.suki;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class Place {
    private final Map<ActionCategory, Integer> actions;

    public Place() {
        this.actions = new EnumMap<>(ActionCategory.class);

        this.actions.put(ActionCategory.STUDY, ActionCategory.STUDY.getStamina());
        this.actions.put(ActionCategory.PART_TIME, ActionCategory.PART_TIME.getStamina());
        this.actions.put(ActionCategory.EXERCISE, ActionCategory.EXERCISE.getStamina());
        this.actions.put(ActionCategory.SLEEP, ActionCategory.SLEEP.getStamina());
    }
}
