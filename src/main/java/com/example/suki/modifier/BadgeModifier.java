package com.example.suki.modifier;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.UserState;

import java.util.List;

public class BadgeModifier {
    public void modify(UserState userState, List<BadgeCategory> badgeList){
        if(badgeList.size() > 6) throw new IllegalArgumentException("뱃지는 최대 6개로 제한됩니다.");

        int totalBonus = badgeList.stream()
                .mapToInt(BadgeCategory::getValue)
                .sum();

        if (totalBonus > 0) {
            userState.applyDeltaToSpecializedPlace(totalBonus, ActionCategory.SLEEP);
        }
    }
}
