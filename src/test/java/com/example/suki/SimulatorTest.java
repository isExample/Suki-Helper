package com.example.suki;

import com.example.suki.domain.UserState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimulatorTest {
    @Test
    void 목표체력은_1_이상_99_이하이다(){
        Simulator simulator = new Simulator();
        UserState userState = new UserState();

        assertThrows(IllegalArgumentException.class, () -> simulator.simulate(userState, 0));
    }
}
