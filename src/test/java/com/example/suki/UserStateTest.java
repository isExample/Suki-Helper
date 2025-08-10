package com.example.suki;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserStateTest {
    @Test
    void 유저상태는_초기화시_기본장소_5곳을_포함한다(){
        UserState userState = new UserState();

        assertTrue(userState.getPlaces().containsKey(PlaceCategory.SCHOOL));
        assertTrue(userState.getPlaces().containsKey(PlaceCategory.HOME));
        assertTrue(userState.getPlaces().containsKey(PlaceCategory.PARK));
        assertTrue(userState.getPlaces().containsKey(PlaceCategory.CAFE));
        assertTrue(userState.getPlaces().containsKey(PlaceCategory.LIBRARY));
        assertEquals(5, userState.getPlaces().size());
    }

    @ParameterizedTest
    @EnumSource(value = PlaceCategory.class, names = {
            "GOLD_MINE","ART_GALLERY","GYM","PC_ROOM","FOOTBALL_PITCH","WORKSHOP","PRACTICE_ROOM"
    })
    void 기본_유저상태는_조건부장소를_포함하지_않는다(PlaceCategory placeCategory){
        UserState us = new UserState();

        assertFalse(us.getPlaces().containsKey(placeCategory));
    }
}
