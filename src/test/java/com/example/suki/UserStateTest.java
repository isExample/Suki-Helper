package com.example.suki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
