package com.example.suki;

public interface ItemEffect {
    void apply(UserState userState);
    int deltaFor(PlaceCategory place, ActionCategory action);
}
