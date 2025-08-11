package com.example.suki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeModifierTest {
    @Test
    void 잠자기특화장소의_잠자기_체력지수를_증가시키는_뱃지가_존재한다(){
        BadgeModifier modifier = new BadgeModifier();
        UserState userState = new UserState();
        BadgeCategory badge = BadgeCategory.UNIV_1;
        int baseStamina = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);
        int bonusStamina = badge.getValue();

        modifier.modify(userState, badge);

        assertEquals(baseStamina + bonusStamina, userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP));
    }
}
