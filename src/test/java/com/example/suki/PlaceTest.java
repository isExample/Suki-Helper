package com.example.suki;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

    @ParameterizedTest
    @EnumSource(value = PlaceCategory.class, names = "SCHOOL", mode = EnumSource.Mode.EXCLUDE)
    void 특수행동_수업듣기는_학교에만_존재한다(PlaceCategory placeCategory){
        Place place = new Place(placeCategory);

        assertTrue(!place.getActions().containsKey(ActionCategory.ATTEND_CLASS));
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
}
