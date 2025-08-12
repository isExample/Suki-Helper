package com.example.suki.modifier;

import com.example.suki.domain.item.ItemCategory;
import com.example.suki.domain.UserState;

import java.util.List;

public class ItemModifier {
    public void modify(UserState userState, List<ItemCategory> itemList){
        for(ItemCategory item : itemList){
            item.apply(userState);
        }
    }
}
