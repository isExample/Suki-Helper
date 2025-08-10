package com.example.suki;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class UserState {
    private final Map<PlaceCategory, Place> places;

    public UserState() {
        this.places = new EnumMap<>(PlaceCategory.class);

        this.places.put(PlaceCategory.SCHOOL, new Place(PlaceCategory.SCHOOL));
        this.places.put(PlaceCategory.HOME, new Place(PlaceCategory.HOME));
        this.places.put(PlaceCategory.PARK, new Place(PlaceCategory.PARK));
        this.places.put(PlaceCategory.CAFE, new Place(PlaceCategory.CAFE));
        this.places.put(PlaceCategory.LIBRARY, new Place(PlaceCategory.LIBRARY));
    }
}
