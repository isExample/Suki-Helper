package com.example.suki;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.modifier.BadgeModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadgeModifierTest {
    private BadgeModifier modifier;
    private UserState userState;

    @BeforeEach
    void setUp(){
        modifier = new BadgeModifier();
        userState = new UserState();
        userState.activatePlace(PlaceCategory.ART_GALLERY);
    }


    public static Stream<PlaceCategory> 잠자기특화장소() {
        return Arrays.stream(PlaceCategory.values())
                .filter(p -> p.isSpecializedFor(ActionCategory.SLEEP));
    }

    public static Stream<BadgeCategory> 잠자기특화뱃지() {
        return Arrays.stream(BadgeCategory.values())
                .filter(b -> b.getActionCategory() == ActionCategory.SLEEP);
    }

    public static Stream<Arguments> 잠자기특화장소x뱃지() {
        List<PlaceCategory> sleepPlaces = 잠자기특화장소().toList();
        return 잠자기특화뱃지()
                .flatMap(b -> sleepPlaces.stream().map(p -> Arguments.of(b, p)));
    }

    @ParameterizedTest
    @MethodSource("잠자기특화장소x뱃지")
    void 잠자기특화장소의_잠자기_체력지수를_증가시키는_뱃지가_존재한다(BadgeCategory badge, PlaceCategory place){
        int baseStamina = userState.getPlaces().get(place).getActions().get(ActionCategory.SLEEP);
        int bonusStamina = badge.getValue();

        modifier.modify(userState, List.of(badge));

        assertEquals(baseStamina + bonusStamina, userState.getPlaces().get(place).getActions().get(ActionCategory.SLEEP));
    }

    @ParameterizedTest
    @MethodSource("잠자기특화장소")
    void 잠자기특화뱃지_효과는_중첩된다(PlaceCategory place){
        List<BadgeCategory> badgeList = 잠자기특화뱃지().toList();
        int baseStamina = userState.getPlaces().get(place).getActions().get(ActionCategory.SLEEP);
        int totalBonus = badgeList.stream()
                .mapToInt(BadgeCategory::getValue)
                .sum();

        modifier.modify(userState, badgeList);

        assertEquals(baseStamina + totalBonus, userState.getPlaces().get(place).getActions().get(ActionCategory.SLEEP));
    }
}
