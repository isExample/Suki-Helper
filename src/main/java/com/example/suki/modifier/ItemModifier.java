package com.example.suki.modifier;

import com.example.suki.domain.item.ItemCategory;
import com.example.suki.domain.UserState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemModifier {
    public void modify(UserState userState, List<ItemCategory> itemList){
        for(ItemCategory item : itemList){
            item.apply(userState);
        }
    }
}
