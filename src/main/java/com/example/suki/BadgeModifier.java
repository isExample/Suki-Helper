package com.example.suki;

import java.util.List;

public class BadgeModifier {
    public void modify(UserState userState, List<BadgeCategory> badgeList){
        for(BadgeCategory badge : badgeList){
            userState.applyDeltaToSpecializedPlace(badge.getValue(), badge.getActionCategory());
        }

    }
}
