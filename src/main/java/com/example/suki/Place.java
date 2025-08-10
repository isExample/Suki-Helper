package com.example.suki;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Place {
    private final Set<ActionCategory> actions;

    public Place() {
        this.actions = new HashSet<>();

        this.actions.add(ActionCategory.STUDY);
        this.actions.add(ActionCategory.PART_TIME);
        this.actions.add(ActionCategory.EXERCISE);
        this.actions.add(ActionCategory.SLEEP);
    }
}
