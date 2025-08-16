package com.example.suki.application;

import com.example.suki.domain.simulation.Simulator;
import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.api.dto.SimulationResponse;
import com.example.suki.domain.User.UserContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.User.UserStateFactory;
import com.example.suki.domain.modifier.BadgeModifier;
import com.example.suki.domain.modifier.FitnessLevelModifier;
import com.example.suki.domain.modifier.ItemModifier;
import com.example.suki.domain.modifier.TraitModifier;
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

    public SimulationResponse simulateReach(SimulationRequest request) {
        UserState userState = userStateFactory.create(UserContext.from(request));

        applyModifiers(userState, request);

        return SimulationResponse.from(request.targetStamina(), simulator.simulateReach(userState, request.targetStamina()));
    }

    public SimulationResponse simulateFinishAt(SimulationRequest request) {
        UserState userState = userStateFactory.create(UserContext.from(request));

        applyModifiers(userState, request);

        return SimulationResponse.from(request.targetStamina(), null);
    }

    private void applyModifiers(UserState userState, SimulationRequest request) {
        fitnessLevelModifier.modify(userState, request.fitnessLevel());
        badgeModifier.modify(userState, request.badgeList());
        traitModifier.modify(userState, request.traitList());
        itemModifier.modify(userState, request.itemList());
    }
}
