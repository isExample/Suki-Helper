package com.example.suki;

public class PlaceModifier {
    public void modify(UserState userState, PlaceCategory placeCategory){
        userState.deactivatePlace(placeCategory);
    }
}
