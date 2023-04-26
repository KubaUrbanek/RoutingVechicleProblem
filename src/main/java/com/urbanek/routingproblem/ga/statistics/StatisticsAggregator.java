package com.urbanek.routingproblem.ga.statistics;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class StatisticsAggregator {
    private final List<GenerationStatistics> generationStats = new ArrayList<>();

    public GenerationStatistics getSeriesWithBestResult() {
        LocationRandomKeySeries bestRandomKeySeries = generationStats.stream()
                .map(GenerationStatistics::getBestResult)
                .min(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
        return findLocationWithRandomKeySeries(bestRandomKeySeries);
    }


    public GenerationStatistics getSeriesWithWorstResult() {
        LocationRandomKeySeries worstRandomKeySeries = generationStats.stream()
                .map(GenerationStatistics::getWorstResult)
                .max(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
        return findLocationWithRandomKeySeries(worstRandomKeySeries);
    }

    public List<GenerationStatistics> getStats() {
        return generationStats;
    }

    public void reset() {
        generationStats.clear();
    }

    public void addGenerationStat(int generationNumber, List<LocationRandomKeySeries> locationRandomKeySeries) {
        generationStats.add(GenerationStatistics.builder()
                .generation(locationRandomKeySeries)
                .generationNumber(generationNumber)
                .build());
    }


    private GenerationStatistics findLocationWithRandomKeySeries(LocationRandomKeySeries randomKeySeries) {
        return generationStats.stream()
                .filter(series -> series.getGeneration()
                        .stream()
                        .anyMatch(locationRandomKeySeries -> randomKeySeries == locationRandomKeySeries))
                .findFirst().orElseThrow();
    }
}
