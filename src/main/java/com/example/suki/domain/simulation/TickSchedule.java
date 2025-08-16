package com.example.suki.domain.simulation;

import com.example.suki.domain.place.PlaceCategory;

@FunctionalInterface
public interface TickSchedule {
    PlaceCategory placeAt(int tick, PlaceCategory second);
}
