package com.example.suki.domain.modifier;

import com.example.suki.application.ModifierContext;
import com.example.suki.domain.User.UserState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStateModifier {
    private final FitnessLevelModifier fitnessLevelModifier;
    private final BadgeModifier badgeModifier;
    private final TraitModifier traitModifier;
    private final ItemModifier itemModifier;

    public void apply(UserState userState, ModifierContext context){
        fitnessLevelModifier.modify(userState, context.fitnessLevel());
        badgeModifier.modify(userState, context.badgeList());
        traitModifier.modify(userState, context.traitList());
        itemModifier.modify(userState, context.permanentItemList());
    }
}
