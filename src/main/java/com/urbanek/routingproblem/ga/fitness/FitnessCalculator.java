package com.urbanek.routingproblem.ga.fitness;

import com.urbanek.routingproblem.ga.caches.DistanceFitnessCache;
import com.urbanek.routingproblem.ga.caches.SeriesFitnessCalculatorCache;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.distances.services.DistanceIdentifierFactory;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import com.urbanek.routingproblem.geo.locations.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FitnessCalculator {
    private final DistanceFitnessCache distanceFitnessCache;
    private final SeriesFitnessCalculatorCache seriesFitnessCalculatorCache;
    private final DistanceIdentifierFactory distanceIdentifierFactory;
    private final LocationService locationService;

    public double calculateFitness(Map<DistanceIdentifier, Double> distances, List<LocationRandomKey> locationRandomKeys) {
        Map<String, List<Location>> orderedLocationIndexesGroupByEmployee = locationService.getOrderedLocationGroupByEmployee(locationRandomKeys);
        return getFitnessValue(distances, orderedLocationIndexesGroupByEmployee);
    }

    private double getFitnessValue(Map<DistanceIdentifier, Double> distances, Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        String seriesIdentifier = prepareSeriesIdentifier(orderedLocationIndexesGroupByEmployee);
        Double cacheValue = seriesFitnessCalculatorCache.getFitnessValue(seriesIdentifier);
        if (Objects.nonNull(cacheValue)) {
            return cacheValue;
        } else {
            Double fitnessValue = orderedLocationIndexesGroupByEmployee.keySet()
                    .stream()
                    .map(orderedLocationIndexesGroupByEmployee::get)
                    .map((locations) -> getDistanceFitnessValue(distances, locations))
                    .reduce(Double::sum)
                    .orElseThrow();
            seriesFitnessCalculatorCache.notify(seriesIdentifier, fitnessValue);
            return fitnessValue;
        }
    }

    private String prepareSeriesIdentifier(Map<String, List<Location>> orderedLocationIndexesGroupByEmployee) {
        StringJoiner joiner = new StringJoiner("-");
        orderedLocationIndexesGroupByEmployee.forEach((key, value) -> {
            joiner.add(key);
            value.forEach(location ->
                    joiner.add(String.valueOf(location.id())));
        });
        return joiner.toString();
    }

    private Double getDistanceFitnessValue(Map<DistanceIdentifier, Double> distances, List<Location> locations) {
        String seriesIdentifier = generateLocationSeriesIdentifier(locations);

        Double cachedValue = distanceFitnessCache.getFitnessValue(seriesIdentifier);

        if (Objects.isNull(cachedValue)) {
            Double distanceFitnessValue = calculateDistanceFitnessValue(distances, locations);
            distanceFitnessCache.notify(seriesIdentifier, distanceFitnessValue);
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
