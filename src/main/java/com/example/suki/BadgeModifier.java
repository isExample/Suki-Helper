package com.example.suki;

public class BadgeModifier {
    public void modify(UserState userState, BadgeCategory badge){
        int bonus = badge.getValue();
        userState.applyDeltaToSpecializedPlace(bonus, badge.getActionCategory());
    }
}
