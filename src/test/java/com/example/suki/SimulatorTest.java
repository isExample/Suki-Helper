package com.example.suki;

import com.example.suki.domain.day.DayCategory;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.Simulator;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.place.PlaceCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SimulatorTest {

    @InjectMocks
    private Simulator simulator;

    @Mock
    private AlgorithmStrategy strategy;

    @Test
    void 평일_시뮬레이션_시_올바른_스케줄과_장소로_전략을_호출한다() {
        UserState userState = new UserState(DayCategory.WEEKDAY_OTHER);
        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.SCHOOL); // 평일 필수 장소
        userState.activatePlace(PlaceCategory.HOME);   // 오후 장소 1
        userState.activatePlace(PlaceCategory.PARK);   // 오후 장소 2

        int targetStamina = 60;
        ReachGoal goal = new ReachGoal(targetStamina);
        ArgumentCaptor<SimulationContext> contextCaptor = ArgumentCaptor.forClass(SimulationContext.class);

        simulator.simulateReach(userState, targetStamina, Map.of(), strategy);

        // userState에 활성화된 장소(SCHOOL, HOME, PARK)의 수만큼 strategy.solve()가 호출되었는지 검증
        verify(strategy, times(3)).solve(contextCaptor.capture());

        List<SimulationContext> capturedContexts = contextCaptor.getAllValues();
        assertThat(capturedContexts).allSatisfy(context -> {
            // 평일 스케줄이 올바르게 전달되었는지 확인
            assertThat(context.schedule()).isNotNull();
            // 0틱일 때 장소는 SCHOOL이어야 함
            assertThat(context.schedule().placeAt(0, context.secondPlace())).isEqualTo(PlaceCategory.SCHOOL);
            // 6틱 이후에는 secondPlace여야 함
            assertThat(context.schedule().placeAt(7, context.secondPlace())).isEqualTo(context.secondPlace());
            // Goal과 UserState가 올바르게 전달되었는지 확인
            assertThat(context.goal()).isEqualTo(goal);
            assertThat(context.userState()).isEqualTo(userState);
        });

        // secondPlace가 HOME, PARK, SCHOOL로 각각 한 번씩 호출되었는지 확인
        assertThat(capturedContexts)
                .map(SimulationContext::secondPlace)
                .containsExactlyInAnyOrder(PlaceCategory.HOME, PlaceCategory.PARK, PlaceCategory.SCHOOL);
    }

    @Test
    void 주말_시뮬레이션_시_올바른_스케줄과_장소로_전략을_호출한다() {
        UserState userState = new UserState(DayCategory.WEEKEND);
        userState.deactivateAll();
        userState.activatePlace(PlaceCategory.HOME);   // 장소 1
        userState.activatePlace(PlaceCategory.PARK);   // 장소 2

        int targetStamina = 70;
        ReachGoal goal = new ReachGoal(targetStamina);
        ArgumentCaptor<SimulationContext> contextCaptor = ArgumentCaptor.forClass(SimulationContext.class);

        simulator.simulateReach(userState, targetStamina, Map.of(), strategy);

        // userState에 활성화된 장소(HOME, PARK)의 수만큼 strategy.solve()가 호출되었는지 검증
        verify(strategy, times(2)).solve(contextCaptor.capture());

        List<SimulationContext> capturedContexts = contextCaptor.getAllValues();
        assertThat(capturedContexts).allSatisfy(context -> {
            // 평일 스케줄이 올바르게 전달되었는지 확인
            assertThat(context.schedule()).isNotNull();
            // 0틱, 6틱 이후 모두 secondPlace여야 함
            assertThat(context.schedule().placeAt(0, context.secondPlace())).isEqualTo(context.secondPlace());
            assertThat(context.schedule().placeAt(7, context.secondPlace())).isEqualTo(context.secondPlace());
            // Goal과 UserState가 올바르게 전달되었는지 확인
            assertThat(context.goal()).isEqualTo(goal);
            assertThat(context.userState()).isEqualTo(userState);
        });

        // secondPlace가 HOME, PARK로 각각 한 번씩 호출되었는지 확인
        assertThat(capturedContexts)
                .map(SimulationContext::secondPlace)
                .containsExactlyInAnyOrder(PlaceCategory.HOME, PlaceCategory.PARK);
    }
}
