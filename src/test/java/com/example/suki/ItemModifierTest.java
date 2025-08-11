package com.example.suki;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemModifierTest {

    static Stream<PlaceCategory> 기본장소() {
        return Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault);
    }

    @ParameterizedTest
    @MethodSource("기본장소")
    void 모든장소_모든행동의_체력소모량을_감소시키는_아이템이_존재한다(PlaceCategory place){
        ItemModifier modifier = new ItemModifier();
        UserState userState = new UserState();
        ItemCategory item = ItemCategory.CALMING_STONE;

        Map<ActionCategory, Integer> before = new EnumMap<>(userState.getPlaces().get(place).getActions());

        modifier.modify(userState, item);

        Map<ActionCategory, Integer> after = userState.getPlaces().get(place).getActions();

        before.forEach((action, base) -> {
            int expected = (base < 0) ? base + 1 : base; // 체력지수가 양수인 잠자기는 감소x
            assertEquals(expected, after.get(action));
        });
    }

    @Test
    void 특정장소_특정행동의_체력지수를_보정하는_아이템이_존재한다(){
        ItemModifier modifier = new ItemModifier();
        UserState userState = new UserState();
        ItemCategory item = ItemCategory.MEMORY_FORM_PILLOW;
        int baseStamina = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);

        modifier.modify(userState, item);

        assertEquals(baseStamina + 2, userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP));
    }
}
