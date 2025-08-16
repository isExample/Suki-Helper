package com.example.suki.application;

import com.example.suki.Simulator;
import com.example.suki.api.SimulationRequest;
import com.example.suki.api.SimulationResponse;
import com.example.suki.domain.User.UserContext;
import com.example.suki.domain.User.UserState;
import com.example.suki.domain.User.UserStateFactory;
import com.example.suki.modifier.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimulationService {
    private final UserStateFactory userStateFactory;
    private final FitnessLevelModifier fitnessLevelModifier;
    private final PlaceModifier placeModifier;
    private final BadgeModifier badgeModifier;
    private final TraitModifier traitModifier;
    private final ItemModifier itemModifier;
    private final Simulator simulator;

    public SimulationResponse applyModifiers(SimulationRequest request) {
        UserState userState = userStateFactory.create(UserContext.from(request));

        fitnessLevelModifier.modify(userState, request.fitnessLevel());
        placeModifier.modify(userState, request.inactiveList(), request.activeList());
        badgeModifier.modify(userState, request.badgeList());
        traitModifier.modify(userState, request.traitList());
        itemModifier.modify(userState, request.itemList());

        return SimulationResponse.from(request.targetStamina(), simulator.simulate(userState, request.targetStamina()));
    }
}
