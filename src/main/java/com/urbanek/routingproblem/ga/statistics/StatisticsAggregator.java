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
                .map(GenerationStatistics::bestResult)
                .min(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
        return findLocationWithRandomKeySeries(bestRandomKeySeries);
    }



    public GenerationStatistics getSeriesWithWorstResult() {
        LocationRandomKeySeries worstRandomKeySeries = generationStats.stream()
                .map(GenerationStatistics::worstResult)
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
        GenerationStatistics.GenerationStatisticsBuilder statsBuilder = GenerationStatistics.builder()
                .generation(locationRandomKeySeries);

        List<LocationRandomKeySeries> sortedGeneration = locationRandomKeySeries.stream()
                .sorted(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .toList();

        statsBuilder
                .bestResult(getSeriesWithBestResult(sortedGeneration))
                .worstResult(getSeriesWithWorstResult(sortedGeneration))
                .averageFitness(getAverageFitness(sortedGeneration))
                .totalFitness(getTotalFitness(sortedGeneration))
                .generationNumber(generationNumber);

        generationStats.add(statsBuilder.build());
    }

    private static LocationRandomKeySeries getSeriesWithWorstResult(List<LocationRandomKeySeries> sortedGeneration) {
        return sortedGeneration.get(sortedGeneration.size() - 1);
    }

    private static LocationRandomKeySeries getSeriesWithBestResult(List<LocationRandomKeySeries> sortedGeneration) {
        return sortedGeneration.get(0);
    }

    private static double getTotalFitness(List<LocationRandomKeySeries> sortedGeneration) {
        return sortedGeneration.stream()
                .mapToDouble(LocationRandomKeySeries::fitnessScore)
                .sum();
    }

    private static double getAverageFitness(List<LocationRandomKeySeries> sortedGeneration) {
        return sortedGeneration.stream()
                .mapToDouble(LocationRandomKeySeries::fitnessScore)
                .average()
                .orElseThrow();
    }

    private GenerationStatistics findLocationWithRandomKeySeries(LocationRandomKeySeries randomKeySeries) {
        return generationStats.stream()
                .filter(series -> series.generation()
                        .stream()
                        .anyMatch(locationRandomKeySeries -> randomKeySeries == locationRandomKeySeries))
                .findFirst().orElseThrow();
    }
}
