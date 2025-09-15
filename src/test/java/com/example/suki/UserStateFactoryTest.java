package com.example.suki;

import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.domain.User.UserContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.User.UserStateFactory;
import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.place.PlaceCategory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserStateFactoryTest {
    private final UserStateFactory userStateFactory = new UserStateFactory();

    @Test
    void 장소를_올바르게_활성화_및_비활성화시킨다() {
        SimulationRequest request = new SimulationRequest(
                60,
                5,
                DayCategory.WEEKDAY_OTHER,
                List.of(PlaceCategory.HOME), // 비활성화할 장소
                List.of(PlaceCategory.GOLD_MINE), // 활성화할 장소
                List.of(), // badgeList
                List.of(), // traitList
                List.of(), // permanentItemList
                Map.of()   // consumableItemMap
        );
        UserContext userContext = UserContext.from(request);

        UserState actualUserState = userStateFactory.create(userContext);

        // 비활성화된 장소가 실제로 없는지 확인
        assertThat(actualUserState.getPlaces())
                .doesNotContainKey(PlaceCategory.HOME);
        // 활성화된 장소가 실제로 존재하는지 확인
        assertThat(actualUserState.getPlaces())
                .containsKey(PlaceCategory.GOLD_MINE);

        // 비활성화 목록에 없던 다른 기본 장소는 그대로 존재하는지 확인
        assertThat(actualUserState.getPlaces())
                .containsKey(PlaceCategory.LIBRARY);
    }

    @Test
    void 활성화_및_비활성화_목록이_비어있다면_기본_장소만_존재한다() {
        SimulationRequest request = new SimulationRequest(
                60,
                5,
                DayCategory.WEEKDAY_OTHER,
                List.of(), // 비활성화할 장소
                List.of(), // 활성화할 장소
                List.of(),
                List.of(),
                List.of(),
                Map.of()
        );
        UserContext userContext = UserContext.from(request);

        UserState actualUserState = userStateFactory.create(userContext);

        Set<PlaceCategory> expectedDefaultPlaces = Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault)
                .collect(Collectors.toSet());
        // 기본 장소만 존재하는지 확인
        assertThat(actualUserState.getPlaces().keySet()).isEqualTo(expectedDefaultPlaces);
    }
}
