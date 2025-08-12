package com.example.suki.domain.item;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    CALMING_STONE("만지면 마음이 편안해지는 돌", "체력 소모량 -1",
            new All(1)),
    MEMORY_FORM_PILLOW("메모리폼 베개", "집에서 잠자기 효율 +2",
            new PlaceAndAction(2, PlaceCategory.HOME, ActionCategory.SLEEP));

    private final String name;
    private final String description;
    private final ItemEffect effect;

    public void apply(UserState userState){
        effect.apply(userState);
    }
}
