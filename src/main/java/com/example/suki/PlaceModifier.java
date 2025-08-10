package com.example.suki;

public class PlaceModifier {
    public void modify(UserState userState, PlaceCategory inactive, PlaceCategory active){
        userState.deactivatePlace(inactive);
        userState.activatePlace(active);
    }
}
