package com.example.suki.domain.item;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;

public interface ItemEffect {
    void apply(UserState userState);
    int deltaFor(PlaceCategory place, ActionCategory action);
}
