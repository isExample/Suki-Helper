package com.example.suki.domain.effect;

import com.example.suki.domain.DayCategory;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record DayScope(int delta, DayCategory day) implements StaminaEffect {
    public void apply(UserState userState){
        userState.applyDeltaOnMatchingDay(delta, day);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a, DayCategory d){
        return (d == day && a != ActionCategory.SLEEP) ? delta : 0;
    }
}
