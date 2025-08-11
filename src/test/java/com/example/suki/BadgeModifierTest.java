package com.example.suki;

import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeModifierTest {
    static Stream<Arguments> 잠자기특화_장소x뱃지() {
        List<PlaceCategory> sleepPlaces =
                Arrays.stream(PlaceCategory.values())
                        .filter(p -> p.isSpecializedFor(ActionCategory.SLEEP))
                        .toList();

        return Arrays.stream(BadgeCategory.values())
                .filter(b -> b.getActionCategory() == ActionCategory.SLEEP)
                .flatMap(b -> sleepPlaces.stream().map(p -> Arguments.of(b, p)));
    }

    @ParameterizedTest
    @MethodSource("잠자기특화_장소x뱃지")
    void 잠자기특화장소의_잠자기_체력지수를_증가시키는_뱃지가_존재한다(BadgeCategory badge, PlaceCategory place){
        BadgeModifier modifier = new BadgeModifier();
        UserState userState = new UserState();
        userState.activatePlace(PlaceCategory.ART_GALLERY);

        int baseStamina = userState.getPlaces().get(place).getActions().get(ActionCategory.SLEEP);
        int bonusStamina = badge.getValue();

        modifier.modify(userState, List.of(badge));

        assertEquals(baseStamina + bonusStamina, userState.getPlaces().get(place).getActions().get(ActionCategory.SLEEP));
    }

    @Test
    void 잠자기특화뱃지_효과는_중첩된다(){
        BadgeModifier modifier = new BadgeModifier();
        UserState userState = new UserState();

        BadgeCategory badge1 = BadgeCategory.UNIV_1;
        BadgeCategory badge2 = BadgeCategory.UNIV_2;
        int baseStamina = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);
        int totalBonus = badge1.getValue() + badge2.getValue();

        modifier.modify(userState, List.of(badge1, badge2));

        assertEquals(baseStamina + totalBonus, userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP));
    }
}
