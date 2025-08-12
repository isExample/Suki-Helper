package com.example.suki;

public record All(int delta) implements ItemEffect{
    public void apply(UserState userState){
        userState.applyDeltaToAllPlacesExceptSleep(delta);
    }

    public int deltaFor(PlaceCategory p, ActionCategory a){
        return (a == ActionCategory.SLEEP) ? 0 : delta;
    }
}
