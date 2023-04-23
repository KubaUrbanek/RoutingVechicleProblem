package com.urbanek.routingproblem.geo.distances.dtos;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class DistanceIdentifier {
    private final int firstLocationId;
    private final int secondLocationId;
}
