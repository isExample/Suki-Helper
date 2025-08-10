package com.example.suki;

import lombok.Getter;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@Getter
public class UserState {
    private final Map<PlaceCategory, Place> places;

    public UserState() {
        this.places = new EnumMap<>(PlaceCategory.class);

        Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault)
                .forEach(category -> this.places.put(category, new Place(category)));
    }
}
