package com.example.suki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceTest {
    @Test
    void 장소는_초기화시_기본행동_4가지를_포함한다(){
        Place place = new Place();

        assertTrue(place.getActions().containsKey(ActionCategory.STUDY));
        assertTrue(place.getActions().containsKey(ActionCategory.PART_TIME));
        assertTrue(place.getActions().containsKey(ActionCategory.EXERCISE));
        assertTrue(place.getActions().containsKey(ActionCategory.SLEEP));
        assertEquals(4, place.getActions().size());
    }

    @Test
    void 장소내_행동들은_기본체력지수로_초기화된다(){
        Place place = new Place();

        for(ActionCategory action : place.getActions().keySet()){
            System.out.println("행동: " + action + ", 체력: " + place.getActions().get(action));
            assertEquals(action.getStamina(), place.getActions().get(action));
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
}
