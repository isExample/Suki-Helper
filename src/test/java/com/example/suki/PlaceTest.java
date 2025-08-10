package com.example.suki;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceTest {
    @ParameterizedTest
    @EnumSource(PlaceCategory.class)
    void 장소는_초기화시_기본행동_4가지를_포함한다(PlaceCategory placeCategory){
        Place place = new Place(placeCategory);

        assertTrue(place.getActions().containsKey(ActionCategory.STUDY));
        assertTrue(place.getActions().containsKey(ActionCategory.PART_TIME));
        assertTrue(place.getActions().containsKey(ActionCategory.EXERCISE));
        assertTrue(place.getActions().containsKey(ActionCategory.SLEEP));
    }

    @ParameterizedTest
    @EnumSource(PlaceCategory.class)
    void 장소내_행동들은_기본체력지수로_초기화된다(PlaceCategory placeCategory){
        Place place = new Place(placeCategory);

        for(ActionCategory action : place.getActions().keySet()){
            System.out.println("행동: " + action + ", 체력: " + place.getActions().get(action));
            assertEquals(placeCategory.getActions(), place.getActions());
        }
    }

    @Test
    void 학교는_기본행동_4가지와_수업듣기를_포함한다(){
        Place place = new Place(PlaceCategory.SCHOOL);

        assertTrue(place.getActions().containsKey(ActionCategory.ATTEND_CLASS));
        assertTrue(place.getActions().containsKey(ActionCategory.STUDY));
        assertTrue(place.getActions().containsKey(ActionCategory.PART_TIME));
        assertTrue(place.getActions().containsKey(ActionCategory.EXERCISE));
        assertTrue(place.getActions().containsKey(ActionCategory.SLEEP));
        assertEquals(5, place.getActions().size());
    }

    @Test
    void 집은_잠자기_체력지수가_3_증가한다(){
        Place place = new Place(PlaceCategory.HOME);
        int baseSleepStamina = PlaceCategory.SCHOOL.getActions().get(ActionCategory.SLEEP); // 기본행동의 체력지수 참조

        assertEquals(baseSleepStamina + 3, place.getActions().get(ActionCategory.SLEEP));
    }

    @Test
    void 미술관은_잠자기_체력지수가_2_증가한다(){
        Place place = new Place(PlaceCategory.ART_GALLERY);
        int baseSleepStamina = PlaceCategory.SCHOOL.getActions().get(ActionCategory.SLEEP); // 기본행동의 체력지수 참조

        assertEquals(baseSleepStamina + 2, place.getActions().get(ActionCategory.SLEEP));
    }

    @Test
    void 장소생성자에_null_전달시_예외가_발생한다(){
        assertThrows(IllegalArgumentException.class, () -> new Place(null));
    }

    @ParameterizedTest
    @MethodSource("specialActionEntries")
    void 특수행동은_정해진_장소에만_존재한다(ActionCategory action, PlaceCategory owner){
        for (PlaceCategory pc : PlaceCategory.values()) {
            Place p = new Place(pc);
            if (pc == owner) {
                assertTrue(p.getActions().containsKey(action));
            } else {
                assertFalse(p.getActions().containsKey(action));
            }
        }
    }

    static Map<ActionCategory, PlaceCategory> SPECIAL_OWNER = Map.of(
            ActionCategory.ATTEND_CLASS, PlaceCategory.SCHOOL,
            ActionCategory.PLAY_GAME,    PlaceCategory.PC_ROOM,
            ActionCategory.TRAINING,     PlaceCategory.PRACTICE_ROOM,
            ActionCategory.DRAWING,      PlaceCategory.WORKSHOP,
            ActionCategory.FOOTBALL,     PlaceCategory.FOOTBALL_PITCH
    );

    static Stream<Arguments> specialActionEntries(){
        return SPECIAL_OWNER.entrySet().stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()));
    }
}
