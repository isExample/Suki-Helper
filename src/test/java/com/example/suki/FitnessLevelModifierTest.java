package com.example.suki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FitnessLevelModifierTest {
    private FitnessLevelModifier modifier;
    private UserState userState;

    @BeforeEach
    void setUp(){
        modifier = new FitnessLevelModifier();
        userState = new UserState();
    }

    static Stream<PlaceCategory> 기본장소() {
        return Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault);
    }

    @ParameterizedTest
    @CsvSource({"-1", "11", "100"})
    void 범위밖_운동레벨은_예외를_발생시킨다(int level){
        assertThrows(IllegalArgumentException.class, () -> modifier.modify(userState, level));
    }

    @Test
    void 운동레벨이_최대치면_운동하기_행동은_비활성화된다(){
        modifier.modify(userState, 10);

        for(Place place : userState.getPlaces().values()) {
            assertTrue(place.getActions().containsKey(ActionCategory.STUDY));       // 다른 기본행동 존재 확인
            assertFalse(place.getActions().containsKey(ActionCategory.EXERCISE));   // 운동하기 비활성화
        }
    }

    @ParameterizedTest
    @MethodSource("기본장소")
    void 운동레벨_1당_행동_체력지수가_1_증가한다(PlaceCategory placeCategory){
        int fitnessLevel = 5;
        Place place = userState.getPlaces().get(placeCategory);

        modifier.modify(userState, fitnessLevel);

        for(ActionCategory action : place.getActions().keySet()) {
            int baseStamina = placeCategory.getActions().get(action);
            assertEquals(baseStamina + fitnessLevel, place.getActions().get(action));
        }
    }
}
