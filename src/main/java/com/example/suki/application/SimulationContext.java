package com.example.suki.application;

import com.example.suki.api.dto.SimulationRangeRequest;
import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.item.PermanentItemCategory;
import com.example.suki.domain.trait.TraitCategory;

import java.util.List;

public record SimulationContext(
        int fitnessLevel,
        List<BadgeCategory> badgeList,
        List<TraitCategory> traitList,
        List<PermanentItemCategory> itemList
) {
    public static SimulationContext from(SimulationRequest request) {
        return new SimulationContext(
                request.fitnessLevel(),
                request.badgeList(),
                request.traitList(),
                request.itemList()
        );
    }

    public static SimulationContext from(SimulationRangeRequest request) {
        return new SimulationContext(
                request.fitnessLevel(),
                request.badgeList(),
                request.traitList(),
                request.itemList()
        );
    }
}
