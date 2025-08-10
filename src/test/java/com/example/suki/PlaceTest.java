package com.example.suki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaceTest {
    @Test
    void 장소는_기본_행동_4가지를_포함한다(){
        Place place = new Place();

        assertTrue(place.getActions().contains(ActionCategory.STUDY));
        assertTrue(place.getActions().contains(ActionCategory.PART_TIME));
        assertTrue(place.getActions().contains(ActionCategory.EXERCISE));
        assertTrue(place.getActions().contains(ActionCategory.SLEEP));
    }
}
