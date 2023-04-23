package com.urbanek.routingproblem.distances;

import com.urbanek.routingproblem.distances.identifier.DistanceIdentifier;
import com.urbanek.routingproblem.distances.identifier.DistanceIdentifierFactory;
import com.urbanek.routingproblem.locations.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DistanceCalculator {
    private final LocationService locationService;
    private final DistanceIdentifierFactory distanceIdentifierFactory;

    public Map<DistanceIdentifier, Double> calculateDistances() {
        List<Location> locations = locationService.getAllLocations();
        Map<DistanceIdentifier, Double> result = new HashMap<>();
        for (int first = 0; first < locations.size(); first++) {
            Location firstLocation = locations.get(first);
            for (Location secondLocation : locations) {
                result.put(distanceIdentifierFactory.getDistanceIdentifier(firstLocation, secondLocation),
                        calculateDistanceValue(firstLocation, secondLocation));
            }
        }
        return result;
    }

    private double calculateDistanceValue(Location first, Location second) {
        double yDistance = Math.abs(second.y() - first.y());
        double xDistance = Math.abs(second.x() - first.y());
        return Math.hypot(yDistance, xDistance);
    }
}
