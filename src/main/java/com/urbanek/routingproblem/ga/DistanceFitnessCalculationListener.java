package com.urbanek.routingproblem.ga;

public interface DistanceFitnessCalculationListener {
    void notify(String seriesIdentifier, Double fitnessValue);
}
