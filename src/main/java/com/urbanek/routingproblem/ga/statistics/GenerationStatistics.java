package com.urbanek.routingproblem.ga.statistics;

import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import lombok.Builder;

import java.util.List;

@Builder
public record GenerationStatistics(int generationNumber,
                                   LocationRandomKeySeries bestResult,
                                   LocationRandomKeySeries worstResult,
                                   double averageFitness, double totalFitness,
                                   List<LocationRandomKeySeries> generation) {
}
