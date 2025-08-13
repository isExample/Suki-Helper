package com.example.suki;

import com.example.suki.domain.DayCategory;
import com.example.suki.domain.UserState;
import com.example.suki.domain.place.PlaceCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorTest {
    @ParameterizedTest
    @CsvSource({"0", "100"})
    void 목표체력은_1_이상_99_이하이다(int target){
        Simulator simulator = new Simulator();
        UserState userState = new UserState();

        assertThrows(IllegalArgumentException.class, () -> simulator.simulate(userState, target));
    }

    @ParameterizedTest
    @CsvSource({"84"})
    void 주말에_단일장소에서_목표체력에_달성_가능하다(int target){
        Simulator simulator = new Simulator();
        UserState userState = new UserState(DayCategory.WEEKEND);

        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.LIBRARY); // 단일 장소

        assertTrue(simulator.simulate(userState, target).isPossible());
    }
}
