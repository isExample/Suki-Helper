package com.example.suki;

import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.effect.All;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.trait.TraitCategory;
import com.example.suki.modifier.TraitModifier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraitModifierTest {
    static Stream<PlaceCategory> 기본장소(){
        return Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault);
    }

    static Stream<TraitCategory> 모든장소_모든행동_특성(){
        return Arrays.stream(TraitCategory.values())
                .filter(trait -> trait.getEffect() instanceof All);
    }

    public static Stream<Arguments> 모든장소_모든행동_특성x기본장소() {
        List<PlaceCategory> places = 기본장소().toList();
        return 모든장소_모든행동_특성()
                .flatMap(t -> places.stream().map(p -> Arguments.of(t, p)));
    }

    @ParameterizedTest
    @MethodSource("모든장소_모든행동_특성x기본장소")
    void 모든장소_모든행동의_체력소모량을_감소시키는_특성이_존재한다(TraitCategory trait, PlaceCategory place){
        TraitModifier modifier = new TraitModifier();
        UserState userState = new UserState();

        Map<ActionCategory, Integer> before = new EnumMap<>(userState.getPlaces().get(place).getActions());

        modifier.modify(userState, List.of(trait));

        Map<ActionCategory, Integer> after = userState.getPlaces().get(place).getActions();
        before.forEach((action, base) -> {
            assertEquals(base + trait.getEffect().deltaFor(place, action), after.get(action));
        });

    }
}
