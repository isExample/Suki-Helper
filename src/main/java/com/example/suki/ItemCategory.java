package com.example.suki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    CALMING_STONE("만지면 마음이 편안해지는 돌", "체력 소모량 -1",
            userState -> userState.applyDeltaToAllPlacesExceptSleep(1)),
    MEMORY_FORM_PILLOW("메모리폼 베개", "집에서 잠자기 효율 +2",
            userState -> userState.applyDeltaToPlaceAndAction(2, PlaceCategory.HOME, ActionCategory.SLEEP));

    private final String name;
    private final String description;
    private final Consumer<UserState> modifier;

    public void apply(UserState userState){
        modifier.accept(userState);
    }
}
