package com.example.suki;

import lombok.Getter;

@Getter
public enum BadgeCategory {
    UNIV_1("상면대학교", ActionCategory.SLEEP, 2),
    UNIV_2("디지스타", ActionCategory.SLEEP, 3);

    private final String description;
    private final ActionCategory actionCategory;
    private final int value;

    BadgeCategory(String description, ActionCategory actionCategory, int value) {
        this.description = description;
        this.actionCategory = actionCategory;
        this.value = value;
    }
}
