package com.example.suki.domain.item;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public record PlaceAndAction(int delta, PlaceCategory place, ActionCategory action) implements ItemEffect {
    public void apply(UserState userState){
        userState.applyDeltaToPlaceAndAction(delta, place, action);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a){
        return (p == place && a == action) ? delta : 0;
    }
}
