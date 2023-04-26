package com.urbanek.routingproblem.ga.caches;

import com.urbanek.routingproblem.ga.fitness.FitnessCalculationListener;
import com.urbanek.routingproblem.utils.aspects.CacheHit;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SeriesFitnessCalculatorCache implements FitnessCalculationListener {
    private final Map<String, Double> fitnessCache = new HashMap<>();

    @CacheHit
    public Double getFitnessValue(String seriesIdentifier) {
        return fitnessCache.get(seriesIdentifier);
    }

    @Override
    public void notify(String seriesIdentifier, Double fitnessValue) {
        fitnessCache.put(seriesIdentifier, fitnessValue);
    }
}
