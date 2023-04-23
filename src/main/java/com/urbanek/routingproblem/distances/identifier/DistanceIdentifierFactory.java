package com.urbanek.routingproblem.distances.identifier;

import com.urbanek.routingproblem.distances.Location;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DistanceIdentifierFactory {
    private final Map<String, DistanceIdentifier> distanceIdentifiersCache = new HashMap<>();

    public DistanceIdentifier getDistanceIdentifier(Location first, Location second) {
        DistanceIdentifier distanceIdentifier = distanceIdentifiersCache.get(first.id() + "" + second.id());
        if (distanceIdentifier == null) {
            distanceIdentifier = new DistanceIdentifier(first.id(), second.id());
            distanceIdentifiersCache.put(first.id() + "" + second.id(), distanceIdentifier);
        }
        return distanceIdentifier;
    }

}
