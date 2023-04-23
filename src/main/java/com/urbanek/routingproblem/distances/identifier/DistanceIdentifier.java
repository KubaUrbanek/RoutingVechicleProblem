package com.urbanek.routingproblem.distances.identifier;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class DistanceIdentifier {
    private final int firstLocationId;
    private final int secondLocationId;
}
