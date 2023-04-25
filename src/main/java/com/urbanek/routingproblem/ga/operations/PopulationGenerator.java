package com.urbanek.routingproblem.ga.operations;

import com.urbanek.routingproblem.employes.services.EmployeeService;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.fitness.FitnessCalculator;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.geo.distances.dtos.DistanceIdentifier;
import com.urbanek.routingproblem.geo.locations.services.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class PopulationGenerator {
    private final EmployeeService employeeService;
    private final LocationService locationService;
    private final FitnessCalculator fitnessCalculator;

    public List<LocationRandomKeySeries> generateUsingRandomKeys(Map<DistanceIdentifier, Double> distances) {
        return IntStream.rangeClosed(1, Configs.POPULATION_AMOUNT)
                .mapToObj(i -> {
                    List<LocationRandomKey> locationRandomKeys = locationService.getAllLocations().stream()
                            .map(location -> new LocationRandomKey(location.id(), employeeService.getRandomEmployee().getId(), Math.random()))
                            .collect(Collectors.toList());
                    return new LocationRandomKeySeries(locationRandomKeys, fitnessCalculator.calculateFitness(distances, locationRandomKeys)
                    );
                })
                .collect(Collectors.toList());
    }


}
