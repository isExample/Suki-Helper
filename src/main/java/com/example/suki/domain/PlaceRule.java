package com.example.suki.domain;

import com.example.suki.domain.place.PlaceCategory;

@FunctionalInterface
public interface PlaceRule {
    PlaceCategory placeAt(int tick, PlaceCategory second);
}
