package com.urbanek.routingproblem.distances;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class LocationRandomKeySeries {
    private final String seriesIdentifier;
    private final List<LocationRandomKey> locationRandomKeys;
    private final Map<String, List<Location>> orderedLocationGroupByEmployee;

    @Builder
    public LocationRandomKeySeries(List<LocationRandomKey> locationRandomKeys, Map<String, List<Location>> orderedLocationGroupByEmployee) {
        this.locationRandomKeys = locationRandomKeys;
        this.orderedLocationGroupByEmployee = orderedLocationGroupByEmployee;
        this.seriesIdentifier = orderedLocationGroupByEmployee.entrySet()
                .stream()
                .map((entry) -> entry.getKey() + "-" + entry.getValue().stream()
                        .map(location -> String.valueOf(location.id()))
                        .collect(Collectors.joining("-")))
                .collect(Collectors.joining("-"));
    }
}
