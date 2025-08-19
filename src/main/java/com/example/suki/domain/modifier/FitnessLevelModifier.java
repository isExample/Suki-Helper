package com.example.suki.domain.modifier;

import com.example.suki.domain.action.ActionCategory;
import com.example.suki.domain.User.UserState;
import org.springframework.stereotype.Component;

@Component
public class FitnessLevelModifier {
    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 10;

    public void modify(UserState userState, int level) {
        if (level == MIN_LEVEL) return;
        if (level == MAX_LEVEL) userState.disableActionToAllPlaces(ActionCategory.EXERCISE);

        userState.applyDeltaToAllPlacesExceptSleep(level);
    }
}
