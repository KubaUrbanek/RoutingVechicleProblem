package com.urbanek.routingproblem.ga;

import com.urbanek.routingproblem.distances.Location;
import com.urbanek.routingproblem.distances.LocationRandomKeySeries;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ResultPrinter {
    public void printPopulation(List<LocationRandomKeySeries> locationRandomKeySeriesList, Map<String, Double> seriesToFitnessMap) {
        StringJoiner joiner = new StringJoiner("\n");
        IntStream.range(0, locationRandomKeySeriesList.size() - 1)
                .forEach(i -> {
                    joiner.add("Generation " + i + 1);
                    LocationRandomKeySeries locationRandomKeySeries = locationRandomKeySeriesList.get(i);
                    Map<String, List<Location>> orderedLocationGroupByEmployee = locationRandomKeySeries.getOrderedLocationGroupByEmployee();
                    orderedLocationGroupByEmployee.forEach((key, value) ->
                            joiner.add(key + " Route: " + value.stream()
                                    .map(location -> String.valueOf(location.id()))
                                    .collect(Collectors.joining(" -> "))));
                    joiner.add("Total fitness: " + seriesToFitnessMap.get(locationRandomKeySeries.getSeriesIdentifier()));
                    joiner.add("");
                });
        System.out.println(joiner);
    }
}
