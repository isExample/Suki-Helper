package com.example.suki;

import lombok.Getter;

import java.util.Map;

@Getter
public class Place {
    private final Map<ActionCategory, Integer> actions;

    public Place(PlaceCategory placeCategory) {
        if(placeCategory == null) {
            throw new IllegalArgumentException("장소는 null일 수 없습니다.");
        }
        this.actions = Map.copyOf(placeCategory.getActions());
    }
}
