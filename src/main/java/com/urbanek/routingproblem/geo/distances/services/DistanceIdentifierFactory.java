package com.urbanek.routingproblem.geo.distances.services;

import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.locations.dtos.Location;

public interface DistanceIdentifierFactory {
    DistanceIdentifier getDistanceIdentifier(Location first, Location second);
}
