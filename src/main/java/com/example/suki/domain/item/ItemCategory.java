package com.example.suki.domain.item;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.effect.GlobalScope;
import com.example.suki.domain.effect.StaminaEffect;
import com.example.suki.domain.effect.PlaceActionScope;
import com.example.suki.domain.place.PlaceCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    CALMING_STONE("만지면 마음이 편안해지는 돌", "체력 소모량 -1",
            new GlobalScope(1)),
    MEMORY_FORM_PILLOW("메모리폼 베개", "집에서 잠자기 효율 +2",
            new PlaceActionScope(2, PlaceCategory.HOME, ActionCategory.SLEEP));

    private final String name;
    private final String description;
    private final StaminaEffect effect;

    public void apply(UserState userState){
        effect.apply(userState);
    }
}
