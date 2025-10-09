package com.example.suki.domain.simulation;

import com.example.suki.domain.place.Place;
import com.example.suki.domain.place.PlaceCategory;
import com.example.suki.domain.simulation.algorithm.AlgorithmStrategy;
import com.example.suki.domain.simulation.model.SimulationContext;
import com.example.suki.domain.simulation.model.SimulationResult;
import com.example.suki.domain.simulation.model.Tick;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Simulator {
    private static final int WEEKDAY_SCHOOL_TICKS = 6;

    private static final DaySchedule WEEKEND_SCHEDULE = (tick, second) -> second;
    private static final DaySchedule WEEKDAY_SCHEDULE = (tick, second) -> (tick < WEEKDAY_SCHOOL_TICKS ? PlaceCategory.SCHOOL : second);

    public SimulationResult simulate(SimulationContext context, AlgorithmStrategy strategy){
        switch (context.userState().getDay()) {
            case WEEKEND:
                return simulateBySchedule(context, WEEKEND_SCHEDULE, strategy);
            case WEEKDAY_MON:
            case WEEKDAY_OTHER:
                return simulateBySchedule(context, WEEKDAY_SCHEDULE, strategy);
            default:
                return SimulationResult.failure();
        }
    }

    private SimulationResult simulateBySchedule(SimulationContext context, DaySchedule schedule, AlgorithmStrategy strategy) {
        List<List<Tick>> solutions = new ArrayList<>();

        for (Map.Entry<PlaceCategory, Place> entry : context.userState().getPlaces().entrySet()) {
            PlaceCategory place = entry.getKey(); // 평일: 두번째 장소 / 주말: 단일 장소
            SimulationContext executionContext = context.updateExecutionContext(schedule, place, solutions);
            strategy.solve(executionContext);

            if(solutions.size() >= AlgorithmStrategy.MAX_SOLUTIONS) {
                break;
            }
        }
        return solutions.isEmpty() ? SimulationResult.failure() : SimulationResult.success(solutions);
    }
}
