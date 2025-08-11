package com.example.suki;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    CALMING_STONE("만지면 마음이 편안해지는 돌", "체력 소모량 -1", 1);

    private final String name;
    private final String description;
    private final int value;
}
