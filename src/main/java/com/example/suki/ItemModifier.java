package com.example.suki;

public class ItemModifier {
    public void modify(UserState userState, ItemCategory item){
        int value = item.getValue();
        userState.applyDeltaToAllPlacesExceptSleep(value);
    }
}
