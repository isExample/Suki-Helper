package com.example.suki;

import java.util.HashSet;
import java.util.Set;

public class Place {
    public Set<ActionCategory> actions;

    public Place() {
        actions = Set.of(
                ActionCategory.STUDY,
                ActionCategory.PART_TIME,
                ActionCategory.EXERCISE,
                ActionCategory.SLEEP);
    }
}
