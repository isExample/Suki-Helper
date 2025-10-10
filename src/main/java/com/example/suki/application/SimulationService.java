package com.example.suki.application;

import com.example.suki.api.dto.SimulationRangeRequest;
import com.example.suki.api.dto.SimulationRangeResponse;
import com.example.suki.domain.modifier.*;
import com.example.suki.domain.simulation.*;
import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.api.dto.SimulationResponse;
import com.example.suki.domain.User.UserContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.User.UserStateFactory;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategyResolver;
import com.example.suki.domain.simulation.algorithm.AlgorithmType;
import com.example.suki.domain.simulation.goal.FinishAtGoal;
import com.example.suki.domain.simulation.goal.FinishWithinGoal;
import com.example.suki.domain.simulation.goal.Goal;
import com.example.suki.domain.simulation.goal.ReachGoal;
import com.example.suki.domain.simulation.model.ConsumableBag;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.SimulationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SimulationService {
    private final UserStateFactory userStateFactory;
    private final UserStateModifier userStateModifier;
    private final Simulator simulator;
    private final AlgorithmStrategyResolver algorithmResolver;

    public SimulationResponse simulateReach(SimulationRequest request) {
        AlgorithmStrategy strategy = algorithmResolver.find(AlgorithmType.BFS_REACH);
        Goal goal = new ReachGoal(request.targetStamina());

        SimulationContext context = createContext(request, goal);
        SimulationResult result = simulator.simulate(context, strategy);

        return SimulationResponse.from(request.targetStamina(), result);
    }

    public SimulationResponse simulateFinishAt(SimulationRequest request) {
        AlgorithmStrategy strategy = algorithmResolver.find(AlgorithmType.DFS_FINISH);
        Goal goal = new FinishAtGoal(request.targetStamina());

        SimulationContext context = createContext(request, goal);
        SimulationResult result = simulator.simulate(context, strategy);

        return SimulationResponse.from(request.targetStamina(), result);
    }

    public SimulationRangeResponse simulateFinishWithin(SimulationRangeRequest request) {
        AlgorithmStrategy strategy = algorithmResolver.find(AlgorithmType.DFS_FINISH);
        Goal goal = new FinishWithinGoal(request.targetMin(), request.targetMax());

        SimulationContext context = createContext(request, goal);
        SimulationResult result = simulator.simulate(context, strategy);

        return SimulationRangeResponse.from(request.targetMin(), request.targetMax(), result);
    }

    private SimulationContext createContext(SimulationRequest request, Goal goal) {
        UserState userState = userStateFactory.create(UserContext.from(request));
        userStateModifier.apply(userState, ModifierContext.from(request));

        return new SimulationContext(
                userState,
                goal,
                request.currentTick(),
                request.currentStamina(),
                new ConsumableBag(request.consumableItemMap()),
                null,
                null,
                null
        );
    }

    private SimulationContext createContext(SimulationRangeRequest request, Goal goal) {
        UserState userState = userStateFactory.create(UserContext.from(request));
        userStateModifier.apply(userState, ModifierContext.from(request));

        return new SimulationContext(
                userState,
                goal,
                request.currentTick(),
                request.currentStamina(),
                new ConsumableBag(request.consumableItemMap()),
                null,
                null,
                null
        );
    }
}
