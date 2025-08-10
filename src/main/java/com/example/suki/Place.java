package com.example.suki;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class Place {
    private final Map<ActionCategory, Integer> actions;

    public Place(PlaceCategory category) {
        this.actions = Map.copyOf(category.getActions());
    }
}
