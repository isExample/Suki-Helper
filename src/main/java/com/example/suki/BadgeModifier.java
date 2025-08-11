package com.example.suki;

import java.util.List;

public class BadgeModifier {
    public void modify(UserState userState, List<BadgeCategory> badgeList){
        int totalBonus = badgeList.stream()
                .mapToInt(BadgeCategory::getValue)
                .sum();

        if (totalBonus > 0) {
            userState.applyDeltaToSpecializedPlace(totalBonus, ActionCategory.SLEEP);
        }
    }
}
