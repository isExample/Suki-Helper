package com.example.suki.domain.place;

import com.example.suki.api.exception.BusinessException;
import com.example.suki.api.exception.ErrorCode;
import com.example.suki.domain.action.ActionCategory;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class Place {
    private final Map<ActionCategory, Integer> actions;

    public Place(PlaceCategory placeCategory) {
        if(placeCategory == null) {
            throw new BusinessException(ErrorCode.PLACE_REQUIRED);
        }
        this.actions = new EnumMap<>(placeCategory.getActions());
    }

    public void disableAction(ActionCategory action){
        if(action == null) {
            throw new BusinessException(ErrorCode.ACTION_REQUIRED);
        }
        if (this.actions.remove(action) == null) {
            throw new BusinessException(ErrorCode.ACTION_ALREADY_DISABLED);
        }
    }

    public void applyDeltaToActionsExceptSleep(int delta){
        this.actions.replaceAll((action, value) ->
                action == ActionCategory.SLEEP ? value : Math.min(0, value + delta)
        );
    }

    public void applyDeltaToAction(int delta, ActionCategory action){
        this.actions.computeIfPresent(action, (k, v) -> v + delta);
    }
}
