package com.example.suki;

import java.util.List;

public class ItemModifier {
    public void modify(UserState userState, List<ItemCategory> itemList){
        for(ItemCategory item : itemList){
            item.apply(userState);
        }
    }
}
