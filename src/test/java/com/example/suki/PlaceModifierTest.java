package com.example.suki;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceModifierTest {
    @Test
    void 주어진_비활성화된_기본장소는_유저상태에서_제거된다(){
        PlaceModifier modifier = new PlaceModifier();
        UserState userState = new UserState();

        modifier.modify(userState, List.of(PlaceCategory.SCHOOL), List.of());

        assertFalse(userState.getPlaces().containsKey(PlaceCategory.SCHOOL));
    }

    @Test
    void 주어진_활성화된_조건부장소는_유저상태에_추가된다(){
        PlaceModifier modifier = new PlaceModifier();
        UserState userState = new UserState();

        modifier.modify(userState, List.of(), List.of(PlaceCategory.GOLD_MINE));

        assertTrue(userState.getPlaces().containsKey(PlaceCategory.GOLD_MINE));
    }
}
