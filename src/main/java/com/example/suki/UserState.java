package com.example.suki;

import lombok.Getter;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@Getter
public class UserState {
    private final Map<PlaceCategory, Place> places;

    public UserState() {
        this.places = new EnumMap<>(PlaceCategory.class);

        Arrays.stream(PlaceCategory.values())
                .filter(PlaceCategory::isDefault)
                .forEach(category -> this.places.put(category, new Place(category)));
    }

    public void activatePlace(PlaceCategory place) {
        if(this.places.containsKey(place)){
            throw new IllegalArgumentException("이미 활성화된 장소입니다.");
        }
        this.places.put(place, new Place(place));
    }

    public void deactivatePlace(PlaceCategory place) {
        if(!this.places.containsKey(place)){
            throw new IllegalArgumentException("비활성화할 수 없는 장소입니다.");
        }
        this.places.remove(place);
    }

    public void disableActionToAllPlaces(ActionCategory action){
        for (Place place : this.places.values()) {
            place.disableAction(action);
        }
    }

    public void applyDeltaToAllPlacesExceptSleep(int delta){
        for (Place place : this.places.values()) {
            place.applyDeltaToActionsExceptSleep(delta);
        }
    }

    public void applyDeltaToSpecializedPlace(int delta, ActionCategory action){
        this.places.forEach((pc, p) -> {
            if (pc.isSpecializedFor(action)) {
                p.applyDeltaToAction(delta, action);
            }
        });
    }
}
