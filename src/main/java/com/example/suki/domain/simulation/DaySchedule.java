package com.example.suki.domain.simulation;

import com.example.suki.domain.place.PlaceCategory;

@FunctionalInterface
public interface DaySchedule {
    PlaceCategory placeAt(int tick, PlaceCategory second);
}
