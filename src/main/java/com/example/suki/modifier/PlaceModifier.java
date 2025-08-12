package com.example.suki.modifier;

import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.UserState;

import java.util.List;

public class PlaceModifier {
    public void modify(UserState userState, List<PlaceCategory> inactiveList, List<PlaceCategory> activeList){
        for(PlaceCategory place : inactiveList){
            userState.deactivatePlace(place);
        }

        for(PlaceCategory place : activeList){
            userState.activatePlace(place);
        }
    }
}
