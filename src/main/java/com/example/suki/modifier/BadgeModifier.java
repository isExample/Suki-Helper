package com.example.suki.modifier;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.badge.BadgeCategory;
import com.example.suki.domain.User.UserState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BadgeModifier {
    public void modify(UserState userState, List<BadgeCategory> badgeList){
        if(badgeList.size() > 7) throw new IllegalArgumentException("뱃지는 최대 7개로 제한됩니다.");

        int totalBonus = badgeList.stream()
                .mapToInt(BadgeCategory::getValue)
                .sum();

        if (totalBonus > 0) {
            userState.applyDeltaToSpecializedPlace(totalBonus, ActionCategory.SLEEP);
        }
    }
}
