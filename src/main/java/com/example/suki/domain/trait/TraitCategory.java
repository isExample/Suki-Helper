package com.example.suki.domain.trait;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.item.All;
import com.example.suki.domain.item.ItemEffect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TraitCategory {
    ADAMANTINE_BODY("금강불괴", "체력 소모량 -3",
            new All(3));

    private final String name;
    private final String description;
    private final ItemEffect effect;

    public void apply(UserState userState){
        effect.apply(userState);
    }
}
