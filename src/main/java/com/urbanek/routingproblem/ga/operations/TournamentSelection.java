package com.urbanek.routingproblem.ga.operations;

import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.utils.aspects.ExecutionTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class TournamentSelection {
    private final Random rand = new Random();
    @ExecutionTime
    public List<LocationRandomKeySeries> performSelection(List<LocationRandomKeySeries> locationRandomKeySeriesList) {
        List<LocationRandomKeySeries> selectionResult = new ArrayList<>();
        while (selectionResult.size() != locationRandomKeySeriesList.size()) {
            List<LocationRandomKeySeries> locationRandomKeySeriesCopy = new ArrayList<>(locationRandomKeySeriesList);
            LocationRandomKeySeries tournamentWinner = IntStream.range(0, getTournamentSize(locationRandomKeySeriesCopy))
                    .mapToObj(i -> pickRandomLocationSeries(locationRandomKeySeriesCopy))
                    .min(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                    .orElseThrow();
            selectionResult.add(tournamentWinner);
        }
        return selectionResult;
    }

    private static int getTournamentSize(List<LocationRandomKeySeries> route) {
        return Math.min(route.size(), Configs.TOURNAMENT_SIZE);
    }

    private LocationRandomKeySeries pickRandomLocationSeries(List<LocationRandomKeySeries> locationRandomKeySeries) {
        LocationRandomKeySeries result = locationRandomKeySeries.get(rand.nextInt(locationRandomKeySeries.size()));
        locationRandomKeySeries.remove(result);
        return result;
    }
}
