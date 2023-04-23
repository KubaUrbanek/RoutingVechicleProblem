package com.urbanek.routingproblem.ga;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DistanceFitnessCache implements DistanceFitnessCalculationListener {
    private final Map<String, Double> distanceFitnessCache = new HashMap<>();

    public Double getFitnessValue(String seriesIdentifier) {
        return distanceFitnessCache.get(seriesIdentifier);
    }

    @Override
    public void notify(String seriesIdentifier, Double fitnessValue) {
        distanceFitnessCache.put(seriesIdentifier, fitnessValue);
    }
}
