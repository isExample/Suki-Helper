package com.example.suki.domain.item;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record All(int delta) implements ItemEffect {
    public void apply(UserState userState){
        userState.applyDeltaToAllPlacesExceptSleep(delta);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a){
        return (a == ActionCategory.SLEEP) ? 0 : delta;
    }
}
