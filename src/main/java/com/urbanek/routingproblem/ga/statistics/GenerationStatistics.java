package com.urbanek.routingproblem.ga.statistics;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Builder
@Getter
public class GenerationStatistics {
    private int generationNumber;
    private List<LocationRandomKeySeries> generation;

    public LocationRandomKeySeries getBestResult() {
        return generation.stream()
                .min(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
    }

    public LocationRandomKeySeries getWorstResult() {
        return generation.stream()
                .max(Comparator.comparingDouble(LocationRandomKeySeries::fitnessScore))
                .orElseThrow();
    }

    public double getAverageFitness() {
        return generation.stream()
                .mapToDouble(LocationRandomKeySeries::fitnessScore)
                .average()
                .orElseThrow();
    }

    public double getTotalFitness() {
        return generation.stream()
                .mapToDouble(LocationRandomKeySeries::fitnessScore)
                .sum();
    }

}
