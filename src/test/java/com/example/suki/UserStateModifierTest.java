package com.example.suki;

import com.example.suki.application.ModifierContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.item.PermanentItemCategory;
import com.example.suki.domain.modifier.*;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.trait.TraitCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserStateModifierTest {
    private final UserStateModifier userStateModifier = new UserStateModifier(
            new FitnessLevelModifier(),
            new BadgeModifier(),
            new TraitModifier(),
            new ItemModifier()
    );

    @Test
    void 운동레벨과_뱃지가_적용되어_UserState의_행동별_체력_소모량이_올바르게_보정된다() {
        // Given (준비)
        UserState userState = new UserState();
        ModifierContext context = new ModifierContext(
                5,                       // 운동 레벨 5 -> 체력 소모량 -5
                List.of(BadgeCategory.SANGMYEON),   // 상면대학교 뱃지 -> 잠자기 체력 소모량 -2
                List.of(),
                List.of()
        );

        userStateModifier.apply(userState, context);

        // LIBRARY의 STUDY 체력 검증, 기본값: -16 / 기대값: -11
        int studyStaminaDelta = userState.getPlaces().get(PlaceCategory.LIBRARY).getActions().get(ActionCategory.STUDY);
        assertThat(studyStaminaDelta).isEqualTo(-11);

        // HOME의 SLEEP 체력 검증, 기본값: 13 / 기대값: 15
        int sleepStaminaDelta = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);
        assertThat(sleepStaminaDelta).isEqualTo(15);

        // PARK의 EXERCISE 체력 검증, 기본값: -24 / 기대값: -19
        int exerciseStaminaDelta = userState.getPlaces().get(PlaceCategory.PARK).getActions().get(ActionCategory.EXERCISE);
        assertThat(exerciseStaminaDelta).isEqualTo(-19);
    }

    @Test
    void Modifier가_없는_경우_UserState의_체력_소모량은_기본값을_유지한다() {
        UserState userState = new UserState();
        ModifierContext context = new ModifierContext(
                0,
                List.of(),
                List.of(),
                List.of()
        );

        userStateModifier.apply(userState, context);

        // LIBRARY의 STUDY 체력 검증
        int studyStaminaDelta = userState.getPlaces().get(PlaceCategory.LIBRARY).getActions().get(ActionCategory.STUDY);
        assertThat(studyStaminaDelta).isEqualTo(-16);

        // HOME의 SLEEP 체력 검증
        int sleepStaminaDelta = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);
        assertThat(sleepStaminaDelta).isEqualTo(13);
    }

    @Test
    void 운동레벨과_특성이_적용되어_UserState의_행동별_체력_소모량이_올바르게_보정된다() {
        // Given (준비)
        UserState userState = new UserState(DayCategory.WEEKDAY_MON);
        ModifierContext context = new ModifierContext(
                7,   // 운동 레벨 7 -> 체력 소모량 -7
                List.of(),
                // 금강불괴 -> 체력 소모량 -3, 월요일 좋아 -> 체력 소모량 -1
                List.of(TraitCategory.ADAMANTINE_BODY, TraitCategory.MONDAY_LOVER),
                List.of()
        );

        userStateModifier.apply(userState, context);

        // LIBRARY의 STUDY 체력 검증, 기본값: -16 / 기대값: -5
        int studyStaminaDelta = userState.getPlaces().get(PlaceCategory.LIBRARY).getActions().get(ActionCategory.STUDY);
        assertThat(studyStaminaDelta).isEqualTo(-5);

        // HOME의 SLEEP 체력 검증, 기본값: 13 / 기대값: 13
        int sleepStaminaDelta = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);
        assertThat(sleepStaminaDelta).isEqualTo(13);

        // PARK의 EXERCISE 체력 검증, 기본값: -24 / 기대값: -13
        int exerciseStaminaDelta = userState.getPlaces().get(PlaceCategory.PARK).getActions().get(ActionCategory.EXERCISE);
        assertThat(exerciseStaminaDelta).isEqualTo(-13);
    }

    @Test
    void 운동레벨과_특성과_영구아이템이_적용되어_UserState의_행동별_체력_소모량이_올바르게_보정된다() {
        // Given (준비)
        UserState userState = new UserState(DayCategory.WEEKDAY_MON);
        ModifierContext context = new ModifierContext(
                3,   // 운동 레벨 3 -> 체력 소모량 -3
                List.of(),
                // 다독가 -> 공부 시 체력 소모량 -1, 운동체질 -> 운동 시 체력 소모량 -5
                List.of(TraitCategory.AVID_READER, TraitCategory.ATHLETIC),
                List.of(PermanentItemCategory.CALMING_STONE)    // 만지면 마음이 편안해지는 돌 -> 체력 소모량 -1
        );

        userStateModifier.apply(userState, context);

        // LIBRARY의 STUDY 체력 검증, 기본값: -16 / 기대값: -11
        int studyStaminaDelta = userState.getPlaces().get(PlaceCategory.LIBRARY).getActions().get(ActionCategory.STUDY);
        assertThat(studyStaminaDelta).isEqualTo(-11);

        // HOME의 SLEEP 체력 검증, 기본값: 13 / 기대값: 13
        int sleepStaminaDelta = userState.getPlaces().get(PlaceCategory.HOME).getActions().get(ActionCategory.SLEEP);
        assertThat(sleepStaminaDelta).isEqualTo(13);

        // PARK의 EXERCISE 체력 검증, 기본값: -24 / 기대값: -15
        int exerciseStaminaDelta = userState.getPlaces().get(PlaceCategory.PARK).getActions().get(ActionCategory.EXERCISE);
        assertThat(exerciseStaminaDelta).isEqualTo(-15);
    }

    @Test
    void 운동레벨이_최대치면_모든_장소의_운동_행동이_비활성화된다() {
        // Given (준비)
        UserState userState = new UserState(DayCategory.WEEKDAY_MON);
        ModifierContext context = new ModifierContext(
                10,   // 운동 레벨 10 -> 체력 소모량 -10 + EXERCISE 비활성화
                List.of(),
                List.of(TraitCategory.ATHLETIC),  // 운동체질 -> 운동 시 체력 소모량 -5
                List.of()
        );

        userStateModifier.apply(userState, context);

        assertFalse(userState.getPlaces().get(PlaceCategory.LIBRARY).getActions().containsKey(ActionCategory.EXERCISE));
        assertFalse(userState.getPlaces().get(PlaceCategory.PARK).getActions().containsKey(ActionCategory.EXERCISE));
    }
}
