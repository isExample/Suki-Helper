package com.example.suki;

public class PlaceModifier {
    public void modify(UserState userState, PlaceCategory inactive){
        userState.deactivatePlace(inactive);
    }
}
