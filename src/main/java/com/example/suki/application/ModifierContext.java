package com.example.suki.application;

import com.example.suki.api.dto.SimulationRangeRequest;
import com.example.suki.api.dto.SimulationRequest;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.item.PermanentItemCategory;
import com.example.suki.domain.trait.TraitCategory;

import java.util.List;

public record ModifierContext(
        int fitnessLevel,
        List<BadgeCategory> badgeList,
        List<TraitCategory> traitList,
        List<PermanentItemCategory> permanentItemList
) {
    public static ModifierContext from(SimulationRequest request) {
        return new ModifierContext(
                request.fitnessLevel(),
                request.badgeList(),
                request.traitList(),
                request.permanentItemList()
        );
    }

    public static ModifierContext from(SimulationRangeRequest request) {
        return new ModifierContext(
                request.fitnessLevel(),
                request.badgeList(),
                request.traitList(),
                request.permanentItemList()
        );
    }
}
