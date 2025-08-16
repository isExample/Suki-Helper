package com.example.suki;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.simulation.model.SimulationResult;
import com.example.suki.domain.simulation.Simulator;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.modifier.FitnessLevelModifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorTest {
    @ParameterizedTest
    @CsvSource({"84", "68", "4"})
    void 주말에_단일장소에서_목표체력에_달성_가능하다(int target){
        Simulator simulator = new Simulator();
        UserState userState = new UserState(DayCategory.WEEKEND);

        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.LIBRARY); // 단일 장소

        assertTrue(simulator.simulateReach(userState, target).isPossible());
    }

    @Test
    void 행동은_하루_최대_14틱_수행할_수_있다(){
        Simulator simulator = new Simulator();
        UserState userState = new UserState(DayCategory.WEEKEND);
        FitnessLevelModifier modifier = new FitnessLevelModifier();

        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.LIBRARY);
        userState.getPlaces().get(PlaceCategory.LIBRARY).disableAction(ActionCategory.SLEEP);
        userState.getPlaces().get(PlaceCategory.LIBRARY).disableAction(ActionCategory.PART_TIME);

        modifier.modify(userState, 10);
        SimulationResult result = simulator.simulateReach(userState, 10);

        assertFalse(result.isPossible());
    }

    @ParameterizedTest
    @CsvSource({"84", "68", "4"})
    void 평일에_단일장소에서_목표체력에_달성_가능하다(int target){
        Simulator simulator = new Simulator();
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);

        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.SCHOOL); // 평일 첫 장소는 항상 학교 -> 단일 장소는 학교만 가능

        assertTrue(simulator.simulateReach(userState, target).isPossible());
    }

    @Test
    void 평일에_복합장소에서_목표체력에_달성_가능하다(){
        Simulator simulator = new Simulator();
        UserState userState = new UserState(DayCategory.WEEKDAY_MON);

        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.SCHOOL);
        userState.activatePlace(PlaceCategory.HOME);

        assertTrue(simulator.simulateReach(userState, 59).isPossible());
    }
}
