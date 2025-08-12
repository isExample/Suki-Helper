package com.example.suki;

import com.example.suki.domain.DayCategory;
import com.example.suki.domain.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.effect.ActionScope;
import com.example.suki.domain.effect.DayScope;
import com.example.suki.domain.effect.GlobalScope;
import com.example.suki.domain.effect.PlaceScope;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.trait.TraitCategory;
import com.example.suki.modifier.TraitModifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.example.suki.TestSources.특성_스코프;
import static com.example.suki.TestSources.특성x기본장소;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TraitModifierTest {
    private TraitModifier modifier;
    private UserState userState;

    @BeforeEach
    void setUp(){
        modifier = new TraitModifier();
        userState = new UserState();
    }

    static Stream<Arguments> 모든장소_모든행동_특성x기본장소() {
        return 특성x기본장소(특성_스코프(GlobalScope.class));
    }

    static Stream<Arguments> 특정행동_특성x기본장소() {
        return 특성x기본장소(특성_스코프(ActionScope.class));
    }

    static Stream<Arguments> 특정장소_특성x기본장소() {
        return 특성x기본장소(특성_스코프(PlaceScope.class));
    }

    static Stream<Arguments> 특정요일_특성x기본장소() {
        return 특성x기본장소(특성_스코프(DayScope.class));
    }

    @ParameterizedTest
    @MethodSource("모든장소_모든행동_특성x기본장소")
    void 모든장소_모든행동의_체력소모량을_감소시키는_특성이_존재한다(TraitCategory trait, PlaceCategory place){
        assertTraitEffectApplied(trait, place);
    }

    @ParameterizedTest
    @MethodSource("특정행동_특성x기본장소")
    void 특정행동의_체력소모량을_감소시키는_특성이_존재한다(TraitCategory trait, PlaceCategory place){
        assertTraitEffectApplied(trait, place);
    }

    @ParameterizedTest
    @MethodSource("특정장소_특성x기본장소")
    void 특정장소의_체력소모량을_감소시키는_특성이_존재한다(TraitCategory trait, PlaceCategory place){
        assertTraitEffectApplied(trait, place);
    }

    @ParameterizedTest
    @MethodSource("특정요일_특성x기본장소")
    void 특정요일의_체력소모량을_감소시키는_특성이_존재한다(TraitCategory trait, PlaceCategory place){
        userState = new UserState(DayCategory.WEEKDAY_MON);
        assertTraitEffectApplied(trait, place);
    }

    private void assertTraitEffectApplied(TraitCategory trait, PlaceCategory place) {
        Map<ActionCategory, Integer> before = new EnumMap<>(userState.getPlaces().get(place).getActions());

        modifier.modify(userState, List.of(trait));

        Map<ActionCategory, Integer> after = userState.getPlaces().get(place).getActions();
        before.forEach((action, base) -> {
            int expected = base + trait.getEffect().deltaFor(place, action, userState.getDay());
            assertEquals(expected, after.get(action));
        });
    }

    @Test
    void 특성은_최대_6개로_제한된다(){
        List<TraitCategory> traitList = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            traitList.add(TraitCategory.ATHLETIC);
        }

        assertThrows(IllegalArgumentException.class, () -> modifier.modify(userState, traitList));
    }
}
