package com.urbanek.routingproblem.ga.fitness;

import com.urbanek.routingproblem.ga.caches.DistanceFitnessCache;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.distances.services.DistanceIdentifierFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FitnessCalculator {
    private final List<DistanceFitnessCalculationListener> distanceFitnessCalculationListeners;
    private final DistanceFitnessCache distanceFitnessCache;
    private final DistanceIdentifierFactory distanceIdentifierFactory;

    public double calculateFitness(LocationRandomKeySeries locationRandomKeySeries, Map<DistanceIdentifier, Double> distances) {
        Map<String, List<Location>> orderedLocationIndexesGroupByEmployee = locationRandomKeySeries.getOrderedLocationGroupByEmployee();
        return orderedLocationIndexesGroupByEmployee.keySet()
                .stream()
                .map(orderedLocationIndexesGroupByEmployee::get)
                .map((locations) -> getDistanceFitnessValue(distances, locations))
                .reduce(Double::sum)
                .orElseThrow();
    }

    private Double getDistanceFitnessValue(Map<DistanceIdentifier, Double> distances, List<Location> locations) {
        String seriesIdentifier = generateLocationSeriesIdentifier(locations);

        Double cachedValue = distanceFitnessCache.getFitnessValue(seriesIdentifier);

        if (Objects.isNull(cachedValue)) {
            Double distanceFitnessValue = calculateDistanceFitnessValue(distances, locations);
            distanceFitnessCalculationListeners.forEach(listener -> listener.notify(seriesIdentifier, distanceFitnessValue));
            return distanceFitnessValue;
        }
        return cachedValue;
    }

    private Double calculateDistanceFitnessValue(Map<DistanceIdentifier, Double> distances, List<Location> locations) {
        double distanceFitnessValue = 0;

        for (int locationIndex = 0; locationIndex < locations.size() - 1; locationIndex++) {
            double distanceBetweenLocations = distances.get(
                    distanceIdentifierFactory.getDistanceIdentifier(locations.get(locationIndex), locations.get(locationIndex + 1)));
            distanceFitnessValue += distanceBetweenLocations;
        }
        return distanceFitnessValue;
    }

    private String generateLocationSeriesIdentifier(List<Location> locations) {
        return locations.stream()
                .map(location -> String.valueOf(location.id()))
                .collect(Collectors.joining("-"));
    }
}
