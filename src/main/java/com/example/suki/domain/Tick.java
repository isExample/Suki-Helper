package com.example.suki.domain;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record Tick(PlaceCategory place, ActionCategory action) {
}
