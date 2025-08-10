package com.example.suki;

public class FitnessLevelModifier {
    public void modify(UserState userState, int level){
        if(level < 0 | level > 10){
            throw new IllegalArgumentException("운동 레벨은 0 이상 10 이하의 정수여야 합니다.");
        }
    }
}
