package com.urbanek.routingproblem.geo.distances.services;

import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.locations.dtos.Location;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DistanceIdentifierFactoryImpl implements DistanceIdentifierFactory {
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
