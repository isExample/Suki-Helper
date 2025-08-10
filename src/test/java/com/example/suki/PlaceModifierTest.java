package com.example.suki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlaceModifierTest {
    @Test
    void 주어진_비활성화된_기본장소는_유저상태에서_제거된다(){
        PlaceModifier modifier = new PlaceModifier();
        UserState userState = new UserState();

        modifier.modify(userState, PlaceCategory.SCHOOL);

        assertFalse(userState.getPlaces().containsKey(PlaceCategory.SCHOOL));
    }
}
