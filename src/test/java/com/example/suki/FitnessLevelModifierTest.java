package com.example.suki;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.place.Place;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.UserState;
import com.example.suki.modifier.FitnessLevelModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.suki.TestSources.기본장소;
import static org.junit.jupiter.api.Assertions.*;

public class FitnessLevelModifierTest {
    private FitnessLevelModifier modifier;
    private UserState userState;

    @BeforeEach
    void setUp(){
        modifier = new FitnessLevelModifier();
        userState = new UserState();
    }

    static Stream<Arguments> 기본장소x운동레벨() {
        return 기본장소().flatMap(pc ->
                IntStream.rangeClosed(0, 9).boxed()
                        .map(level -> Arguments.of(pc, level)));
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
    @MethodSource("기본장소x운동레벨")
    void 운동레벨_1당_잠자기를_제외한_행동의_체력지수가_1_증가한다(PlaceCategory placeCategory, int level){
        Place place = userState.getPlaces().get(placeCategory);

        modifier.modify(userState, level);

        for(ActionCategory action : place.getActions().keySet()) {
            int baseStamina = placeCategory.getActions().get(action);

            if (action == ActionCategory.SLEEP) {
                assertEquals(baseStamina, place.getActions().get(action)); // 잠자기는 그대로
            } else {
                assertEquals(baseStamina + level, place.getActions().get(action));
            }
        }
    }

    @Test
    void 운동레벨에_따른_행동체력_보정결과는_0을_초과하지_않는다(){
        // 수업듣기, 게임하기, 아이돌연습 해당
        userState.activatePlace(PlaceCategory.PC_ROOM);
        userState.activatePlace(PlaceCategory.PRACTICE_ROOM);

        modifier.modify(userState, 9);

        assertEquals(0, userState.getPlaces().get(PlaceCategory.SCHOOL).getActions().get(ActionCategory.ATTEND_CLASS));
        assertEquals(0, userState.getPlaces().get(PlaceCategory.PC_ROOM).getActions().get(ActionCategory.PLAY_GAME));
        assertEquals(0, userState.getPlaces().get(PlaceCategory.PRACTICE_ROOM).getActions().get(ActionCategory.TRAINING));
    }
}
