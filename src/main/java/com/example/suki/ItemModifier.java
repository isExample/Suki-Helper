package com.example.suki;

public class ItemModifier {
    public void modify(UserState userState, ItemCategory item){
        item.apply(userState);
    }
}
