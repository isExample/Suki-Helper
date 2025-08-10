package com.example.suki;

import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class Place {
    private final Map<ActionCategory, Integer> actions;

    public Place(PlaceCategory placeCategory) {
        if(placeCategory == null) {
            throw new IllegalArgumentException("장소는 null일 수 없습니다.");
        }
        this.actions = new EnumMap<>(placeCategory.getActions());
    }

    public void disableAction(ActionCategory action){
        if(action == null) throw new IllegalArgumentException("행동은 null일 수 없습니다.");
        if(!this.actions.containsKey(action)) throw new IllegalArgumentException("이미 비활성화된 행동입니다.");

        this.actions.remove(action);
    }

    public void applyDeltaToActionsExceptSleep(int delta){
        this.actions.replaceAll((action, value) ->
                action == ActionCategory.SLEEP ? value : Math.min(0, value + delta)
        );
    }
}
