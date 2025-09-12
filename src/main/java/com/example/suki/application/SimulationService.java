package com.example.suki.application;

import com.example.suki.api.dto.SimulationRangeRequest;
import com.example.suki.api.dto.SimulationRangeResponse;
import com.example.suki.domain.simulation.*;
import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.api.dto.SimulationResponse;
import com.example.suki.domain.User.UserContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.User.UserStateFactory;
import com.example.suki.domain.modifier.BadgeModifier;
import com.example.suki.domain.modifier.FitnessLevelModifier;
import com.example.suki.domain.modifier.ItemModifier;
import com.example.suki.domain.modifier.TraitModifier;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategyResolver;
import com.example.suki.domain.simulation.algorithm.AlgorithmType;
import com.example.suki.domain.simulation.model.SimulationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationService {
    private final UserStateFactory userStateFactory;
    private final FitnessLevelModifier fitnessLevelModifier;
    private final BadgeModifier badgeModifier;
    private final TraitModifier traitModifier;
    private final ItemModifier itemModifier;
    private final Simulator simulator;
    private final AlgorithmStrategyResolver algorithmResolver;

    public SimulationResponse simulateReach(SimulationRequest request) {
        UserState userState = userStateFactory.create(UserContext.from(request));

        applyModifiers(userState, SimulationContext.from(request));

        AlgorithmStrategy strategy = algorithmResolver.find(AlgorithmType.BFS_REACH);
        SimulationResult result = simulator.simulateReach(userState, request.targetStamina(), request.consumableItemMap(), strategy);
        return SimulationResponse.from(request.targetStamina(), result);
    }

    public SimulationResponse simulateFinishAt(SimulationRequest request) {
        UserState userState = userStateFactory.create(UserContext.from(request));

        applyModifiers(userState, SimulationContext.from(request));

        AlgorithmStrategy strategy = algorithmResolver.find(AlgorithmType.DFS_FINISH);
        SimulationResult result = simulator.simulateFinishAt(userState, request.targetStamina(), request.consumableItemMap(), strategy);
        return SimulationResponse.from(request.targetStamina(), result);
    }

    public SimulationRangeResponse simulateFinishWithin(SimulationRangeRequest request) {
        UserState userState = userStateFactory.create(UserContext.from(request));

        applyModifiers(userState, SimulationContext.from(request));

        AlgorithmStrategy strategy = algorithmResolver.find(AlgorithmType.DFS_FINISH);
        SimulationResult result = simulator.simulateFinishWithin(userState, request.targetMin(), request.targetMax(), request.consumableItemMap(), strategy);
        return SimulationRangeResponse.from(request.targetMin(), request.targetMax(), result);
    }

    private void applyModifiers(UserState userState, SimulationContext context) {
        fitnessLevelModifier.modify(userState, context.fitnessLevel());
        badgeModifier.modify(userState, context.badgeList());
        traitModifier.modify(userState, context.traitList());
        itemModifier.modify(userState, context.permanentItemList());
    }
}
