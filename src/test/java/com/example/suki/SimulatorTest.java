package com.example.suki;

import com.example.suki.domain.UserState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimulatorTest {
    @ParameterizedTest
    @CsvSource({"0", "100"})
    void 목표체력은_1_이상_99_이하이다(int target){
        Simulator simulator = new Simulator();
        UserState userState = new UserState();

        assertThrows(IllegalArgumentException.class, () -> simulator.simulate(userState, target));
    }
}
