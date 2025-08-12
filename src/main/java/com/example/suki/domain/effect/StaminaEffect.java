package com.example.suki.domain.effect;

import com.example.suki.domain.DayCategory;
import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public interface StaminaEffect {
    void apply(UserState userState);
    int deltaFor(PlaceCategory place, ActionCategory action, DayCategory day);
}
