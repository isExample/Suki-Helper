package com.example.suki;

public record PlaceAndAction(int delta, PlaceCategory place, ActionCategory action) implements ItemEffect {
    public void apply(UserState userState){
        userState.applyDeltaToPlaceAndAction(delta, place, action);
    }
}
