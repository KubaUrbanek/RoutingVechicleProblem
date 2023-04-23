package com.urbanek.routingproblem.geo.distances.services;

import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;

import java.util.Map;

public interface DistanceCalculator {
    Map<DistanceIdentifier, Double> calculateDistances();
}
