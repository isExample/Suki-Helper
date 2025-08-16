package com.example.suki.domain.User;

import com.example.suki.api.SimulationRequest;
import com.example.suki.domain.DayCategory;
import com.example.suki.domain.place.PlaceCategory;

import java.util.List;

public record UserContext(
        DayCategory day,
        List<PlaceCategory> inactiveList,
        List<PlaceCategory> activeList
) {
    public static UserContext from(SimulationRequest request) {
        return new UserContext(request.day(), request.inactiveList(), request.activeList());
    }
}
