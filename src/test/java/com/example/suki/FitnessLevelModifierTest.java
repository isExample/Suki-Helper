package com.example.suki;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class FitnessLevelModifierTest {
    @ParameterizedTest
    @CsvSource({"-1", "11", "100"})
    void 범위밖_운동레벨은_예외를_발생시킨다(int level){
        FitnessLevelModifier modifier = new FitnessLevelModifier();
        UserState userState = new UserState();

        assertThrows(IllegalArgumentException.class, () -> modifier.modify(userState, level));
    }

    @Test
    void 운동레벨이_최대치면_운동하기_행동은_비활성화된다(){
        FitnessLevelModifier modifier = new FitnessLevelModifier();
        UserState userState = new UserState();

        modifier.modify(userState, 10);

        for(Place place : userState.getPlaces().values()) {
            assertTrue(place.getActions().containsKey(ActionCategory.STUDY));       // 다른 기본행동 존재 확인
            assertFalse(place.getActions().containsKey(ActionCategory.EXERCISE));   // 운동하기 비활성화
        }
    }
}
