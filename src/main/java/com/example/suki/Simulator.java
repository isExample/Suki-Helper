package com.example.suki;

import com.example.suki.domain.UserState;

public class Simulator {
    public void simulate(UserState userState, int target){
        if(target < 1 || target > 99) throw new IllegalArgumentException("목표 체력은 1 이상 99 이하여야 합니다.");
    }
}
