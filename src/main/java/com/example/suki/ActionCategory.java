package com.example.suki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionCategory {
    STUDY(-16),          // 공부하기
    PART_TIME(-29),      // 알바하기
    EXERCISE(-24),       // 운동하기
    SLEEP(10);           // 잠자기

    private final int stamina;
}
