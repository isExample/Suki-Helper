package com.example.suki.domain.effect;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record ActionScope(int delta, ActionCategory action) implements StaminaEffect {
    public void apply(UserState userState){
        userState.applyDeltaToAction(delta, action);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a, DayCategory d){
        return (a == action) ? delta : 0;
    }
}
