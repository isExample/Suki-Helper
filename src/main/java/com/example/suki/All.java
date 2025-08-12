package com.example.suki;

public record All(int delta) implements ItemEffect{
    public void apply(UserState userState){
        userState.applyDeltaToAllPlacesExceptSleep(delta);
    }
}
