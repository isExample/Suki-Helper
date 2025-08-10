package com.example.suki;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FitnessLevelModifierTest {
    @ParameterizedTest
    @CsvSource({"-1", "11", "100"})
    void 범위밖_운동레벨은_예외를_발생시킨다(int level){
        FitnessLevelModifier modifier = new FitnessLevelModifier();
        UserState userState = new UserState();

        assertThrows(IllegalArgumentException.class, () -> modifier.modify(userState, level));
    }
}
