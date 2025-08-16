package com.example.suki.domain.effect;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public interface StaminaEffect {
    void apply(UserState userState);
    int deltaFor(PlaceCategory place, ActionCategory action, DayCategory day);
}
