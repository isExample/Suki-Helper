package com.example.suki;

import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.suki.TestSources.기본장소;
import static org.junit.jupiter.api.Assertions.*;

public class UserStateTest {
    private UserState userState;

    @BeforeEach
    void setUp(){
        userState = new UserState();
    }

    @ParameterizedTest
    @MethodSource("com.example.suki.TestSources#기본장소")
    void 기본_유저상태는_각_기본장소를_포함한다(){
        Set<PlaceCategory> expected = 기본장소().collect(Collectors.toSet());

        assertEquals(expected, userState.getPlaces().keySet());
        assertEquals(expected.size(), userState.getPlaces().size());
    }

    @ParameterizedTest
    @MethodSource("com.example.suki.TestSources#조건부장소")
    void 기본_유저상태는_조건부장소를_포함하지_않는다(PlaceCategory placeCategory){
        assertFalse(userState.getPlaces().containsKey(placeCategory));
    }

    @Test
    void 기본_유저상태는_요일정보를_가진다(){
        assertNotNull(userState.getDay());
    }

    @ParameterizedTest
    @MethodSource("com.example.suki.TestSources#조건부장소")
    void 비활성화장소를_비활성화_시도하면_예외가_발생한다(PlaceCategory placeCategory){
        assertThrows(IllegalArgumentException.class, () -> userState.deactivatePlace(placeCategory));
    }

    @ParameterizedTest
    @MethodSource("com.example.suki.TestSources#기본장소")
    void 활성화장소를_활성화_시도하면_예외가_발생한다(PlaceCategory placeCategory){
        assertThrows(IllegalArgumentException.class, () -> userState.activatePlace(placeCategory));
    }
}
