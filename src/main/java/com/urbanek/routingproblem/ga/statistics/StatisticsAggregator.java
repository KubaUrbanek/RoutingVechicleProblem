package com.urbanek.routingproblem.ga.statistics;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StatisticsAggregator {
    private final Map<Integer, GenerationStatistics> generationStats = new HashMap<>();

    public GenerationStatistics getSeriesWithBestResult() {
        LocationRandomKeySeries bestRandomKeySeries = generationStats.values()
                .stream()
                .map(GenerationStatistics::getBestResult)
                .min(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
        return findStatsWithRandomKeySeries(bestRandomKeySeries);
    }


    public GenerationStatistics getSeriesWithWorstResult() {
        LocationRandomKeySeries worstRandomKeySeries = generationStats.values()
                .stream()
                .map(GenerationStatistics::getWorstResult)
                .max(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
        return findStatsWithRandomKeySeries(worstRandomKeySeries);
    }

    public Collection<GenerationStatistics> getStats() {
        return generationStats.values();
    }

    public void reset() {
        generationStats.clear();
    }

    public void addGenerationStat(int generationNumber, List<LocationRandomKeySeries> locationRandomKeySeries) {
        generationStats.put(generationNumber, GenerationStatistics.builder()
                .generation(locationRandomKeySeries)
                .generationNumber(generationNumber)
                .build());
    }


    private GenerationStatistics findStatsWithRandomKeySeries(LocationRandomKeySeries randomKeySeries) {
        return generationStats.values()
                .stream()
                .filter(series -> series.getGeneration()
                        .stream()
                        .anyMatch(locationRandomKeySeries -> randomKeySeries == locationRandomKeySeries))
                .findFirst()
                .orElseThrow();
    }
}
