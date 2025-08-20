package com.example.suki.domain.modifier;

import com.example.suki.domain.item.PermanentItemCategory;
import com.example.suki.domain.User.UserState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemModifier {
    public void modify(UserState userState, List<PermanentItemCategory> itemList){
        for(PermanentItemCategory item : itemList){
            item.apply(userState);
        }
    }
}
