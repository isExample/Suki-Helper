package com.example.suki.domain.effect;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record PlaceActionScope(int delta, PlaceCategory place, ActionCategory action) implements StaminaEffect {
    public void apply(UserState userState){
        userState.applyDeltaToPlaceAndAction(delta, place, action);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a, DayCategory d){
        return (p == place && a == action) ? delta : 0;
    }
}
