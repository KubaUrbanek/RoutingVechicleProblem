package com.urbanek.routingproblem.ga.statistics;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class StatisticsAggregator {
    private final List<GenerationStatistics> generationStats = new ArrayList<>();

    public LocationRandomKeySeries getBestResult() {
        return generationStats.stream()
                .map(GenerationStatistics::bestResult)
                .min(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
    }

    public LocationRandomKeySeries getWorstResult() {
        return generationStats.stream()
                .map(GenerationStatistics::worstResult)
                .max(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
    }

    public List<GenerationStatistics> getStats() {
        return generationStats;
    }

    public void reset() {
        generationStats.clear();
    }

    public void addGenerationStat(int generationNumber, List<LocationRandomKeySeries> locationRandomKeySeries) {
        GenerationStatistics.GenerationStatisticsBuilder statsBuilder = GenerationStatistics.builder()
                .generation(locationRandomKeySeries);

        List<LocationRandomKeySeries> sortedGeneration = locationRandomKeySeries.stream()
                .sorted(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .toList();

        statsBuilder
                .bestResult(sortedGeneration.get(0))
                .worstResult(sortedGeneration.get(sortedGeneration.size() - 1))
                .averageFitness(sortedGeneration.stream()
                        .mapToDouble(LocationRandomKeySeries::fitnessScore)
                        .average()
                        .orElseThrow())
                .totalFitness(sortedGeneration.stream()
                        .mapToDouble(LocationRandomKeySeries::fitnessScore)
                        .sum())
                .generationNumber(generationNumber);

        generationStats.add(statsBuilder.build());
    }
}
