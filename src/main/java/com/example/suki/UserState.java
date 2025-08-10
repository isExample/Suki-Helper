package com.example.suki;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class UserState {
    private final Map<PlaceCategory, Place> places;

    public UserState() {
        this.places = new EnumMap<>(PlaceCategory.class);

        this.places.put(PlaceCategory.SCHOOL, new Place());
        this.places.put(PlaceCategory.HOME, new Place());
        this.places.put(PlaceCategory.PARK, new Place());
        this.places.put(PlaceCategory.CAFE, new Place());
        this.places.put(PlaceCategory.LIBRARY, new Place());
    }
}
