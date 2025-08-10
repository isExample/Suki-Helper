package com.example.suki;

public class FitnessLevelModifier {
    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 10;

    public void modify(UserState userState, int level){
        if(level < MIN_LEVEL || level > MAX_LEVEL){
            throw new IllegalArgumentException("운동 레벨은 0 이상 10 이하의 정수여야 합니다.");
        }

        if(level == MAX_LEVEL){
            userState.disableActionToAllPlaces(ActionCategory.EXERCISE);
        }

        userState.updateStaminaToAllPlaces(level);
    }
}
