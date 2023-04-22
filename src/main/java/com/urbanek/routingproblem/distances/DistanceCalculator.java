package com.urbanek.routingproblem.distances;

import lombok.Getter;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DistanceCalculator {
    public Map<String, Double> calculateDistances(List<Location> locations) {
        return locations.stream()
                .flatMap(firstLocation -> locations.stream()
                        .map(secondLocation -> new Distance(firstLocation, secondLocation, calculateDistanceValue(firstLocation, secondLocation))))
                .collect(Collectors.toUnmodifiableMap(Distance::getDistanceIdentifier, Distance::getDistanceValue));
    }

    private double calculateDistanceValue(Location first, Location second) {
        double yDistance = Math.abs(second.y() - first.y());
        double xDistance = Math.abs(second.x() - first.y());
        return Math.hypot(yDistance, xDistance);
    }

    @Value
    @Getter
    private static class Distance {
        String distanceIdentifier;
        double distanceValue;

        public Distance(Location firstLocation, Location secondLocation, double distanceValue) {
            this.distanceValue = distanceValue;
            this.distanceIdentifier = IdGenerator.generateDistanceId(firstLocation, secondLocation);
        }

    }
}
