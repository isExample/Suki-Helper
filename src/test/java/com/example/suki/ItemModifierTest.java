package com.example.suki;

import com.example.suki.domain.*;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.effect.All;
import com.example.suki.domain.item.ItemCategory;
import com.example.suki.domain.effect.PlaceAndAction;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.modifier.ItemModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemModifierTest {
    private ItemModifier modifier;
    private UserState userState;

    @BeforeEach
    void setUp(){
        modifier = new ItemModifier();
        userState = new UserState();
    }

    static Stream<PlaceCategory> 기본장소() {
        return Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault);
    }

    static Stream<ItemCategory> 모든장소_모든행동_아이템(){
        return Arrays.stream(ItemCategory.values())
                .filter(item -> item.getEffect() instanceof All);
    }

    static Stream<ItemCategory> 특정장소_특정행동_아이템(){
        return Arrays.stream(ItemCategory.values())
                .filter(item -> item.getEffect() instanceof PlaceAndAction);
    }

    public static Stream<Arguments> 모든장소_모든행동_아이템x기본장소() {
        List<PlaceCategory> places = 기본장소().toList();
        return 모든장소_모든행동_아이템()
                .flatMap(i -> places.stream().map(p -> Arguments.of(i, p)));
    }

    public static Stream<Arguments> 특정장소_특정행동_아이템x기본장소() {
        List<PlaceCategory> places = 기본장소().toList();
        return 특정장소_특정행동_아이템()
                .flatMap(i -> places.stream().map(p -> Arguments.of(i, p)));
    }

    @ParameterizedTest
    @MethodSource("모든장소_모든행동_아이템x기본장소")
    void 모든장소_모든행동의_체력소모량을_감소시키는_아이템이_존재한다(ItemCategory item, PlaceCategory place){
        Map<ActionCategory, Integer> before = new EnumMap<>(userState.getPlaces().get(place).getActions());

        modifier.modify(userState, List.of(item));

        Map<ActionCategory, Integer> after = userState.getPlaces().get(place).getActions();
        before.forEach((action, base) -> {
            assertEquals(base + item.getEffect().deltaFor(place, action), after.get(action));
        });
    }

    @ParameterizedTest
    @MethodSource("특정장소_특정행동_아이템x기본장소")
    void 특정장소_특정행동의_체력지수를_보정하는_아이템이_존재한다(ItemCategory item, PlaceCategory place){
        Map<ActionCategory, Integer> before = new EnumMap<>(userState.getPlaces().get(place).getActions());

        modifier.modify(userState, List.of(item));

        Map<ActionCategory, Integer> after = userState.getPlaces().get(place).getActions();
        before.forEach((action, base) -> {
            assertEquals(base + item.getEffect().deltaFor(place, action), after.get(action));
        });
    }
}
