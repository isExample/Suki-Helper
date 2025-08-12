package com.example.suki.domain.effect;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record PlaceScope(int delta, PlaceCategory place) implements StaminaEffect {
    public void apply(UserState userState){
        userState.applyDeltaToPlace(delta, place);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a){
        return (p == place && a != ActionCategory.SLEEP) ? delta : 0;
    }
}
