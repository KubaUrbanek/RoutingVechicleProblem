package com.urbanek.routingproblem.ga.fitness;

public interface DistanceFitnessCalculationListener {
    void notify(String seriesIdentifier, Double fitnessValue);
}
