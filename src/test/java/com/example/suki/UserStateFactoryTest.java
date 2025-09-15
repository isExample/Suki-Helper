package com.example.suki;

import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.domain.User.UserContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.User.UserStateFactory;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserStateFactoryTest {
    private final UserStateFactory userStateFactory = new UserStateFactory();

    @Test
    void 장소를_올바르게_활성화_및_비활성화시킨다() {
        UserContext context = new UserContext(
                DayCategory.WEEKDAY_OTHER,
                List.of(PlaceCategory.HOME), // 비활성화할 장소
                List.of(PlaceCategory.GOLD_MINE) // 활성화할 장소
        );

        UserState actualUserState = userStateFactory.create(context);

        // 기대되는 최종 장소를 담는 Set 생성
        Set<PlaceCategory> expectedPlaces = Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault)
                .collect(Collectors.toCollection(HashSet::new));

        context.inactiveList().forEach(expectedPlaces::remove);
        context.activeList().forEach(expectedPlaces::add);

        assertThat(actualUserState.getPlaces().keySet()).isEqualTo(expectedPlaces);
    }

    @Test
    void 활성화_및_비활성화_목록이_비어있다면_기본_장소만_존재한다() {
        UserContext context = new UserContext(
                DayCategory.WEEKDAY_OTHER,
                List.of(), // 비활성화할 장소
                List.of() // 활성화할 장소
        );

        UserState actualUserState = userStateFactory.create(context);

        // 기본 장소만 존재하는지 확인
        Set<PlaceCategory> expectedDefaultPlaces = Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault)
                .collect(Collectors.toSet());

        assertThat(actualUserState.getPlaces().keySet()).isEqualTo(expectedDefaultPlaces);
    }
}
