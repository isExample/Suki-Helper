package com.example.suki.domain.simulation.model;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record Tick(PlaceCategory place, ActionCategory action) {
}
