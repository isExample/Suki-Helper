package com.example.suki.domain.trait;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.effect.ActionScope;
import com.example.suki.domain.effect.All;
import com.example.suki.domain.effect.StaminaEffect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TraitCategory {
    // S급
    ADAMANTINE_BODY("금강불괴", "체력 소모량 -3",
            new All(3)),
    ATHLETIC("운동체질", "운동 시 체력 소모량 -5",
            new ActionScope(5, ActionCategory.EXERCISE)),
    PART_TIME_MASTER("알바의 달인", "알바 시 체력 소모량 -5",
            new ActionScope(5, ActionCategory.PART_TIME)),

    // A급
    AVID_READER("다독가", "공부 시 체력 소모량 -1",
            new ActionScope(1, ActionCategory.STUDY)),
    JELLYFISH_SLEEP("해파리 수면법", "잠자기 시, 체력 회복량 1 증가",
            new ActionScope(1, ActionCategory.SLEEP));


    private final String name;
    private final String description;
    private final StaminaEffect effect;

    public void apply(UserState userState){
        effect.apply(userState);
    }
}
