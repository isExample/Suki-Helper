package com.example.suki.domain.effect;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record GlobalScope(int delta) implements StaminaEffect {
    public void apply(UserState userState){
        userState.applyDeltaToAllPlacesExceptSleep(delta);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a, DayCategory d){
        return (a == ActionCategory.SLEEP) ? 0 : delta;
    }
}
