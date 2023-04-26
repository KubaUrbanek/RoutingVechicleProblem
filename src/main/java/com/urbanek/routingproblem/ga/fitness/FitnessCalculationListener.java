package com.urbanek.routingproblem.ga.fitness;

public interface FitnessCalculationListener {
    void notify(String seriesIdentifier, Double fitnessValue);
}
