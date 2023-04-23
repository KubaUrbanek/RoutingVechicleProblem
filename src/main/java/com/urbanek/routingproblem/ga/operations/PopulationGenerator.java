package com.urbanek.routingproblem.ga.operations;

import com.urbanek.routingproblem.employes.services.EmployeeService;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKeySeries;
import com.urbanek.routingproblem.geo.locations.services.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class PopulationGenerator {
    private final EmployeeService employeeService;
    private final LocationService locationService;

    public List<LocationRandomKeySeries> generateUsingRandomKeys() {
        return IntStream.rangeClosed(1, Configs.POPULATION_AMOUNT)
                .mapToObj(i -> {
                    List<LocationRandomKey> locationRandomKeys = IntStream.rangeClosed(1, locationService.getAllLocations().size())
                            .mapToObj(index -> new LocationRandomKey(employeeService.getRandomEmployee().getId(), Math.random()))
                            .collect(Collectors.toList());
                    return new LocationRandomKeySeries(locationRandomKeys, locationService.getOrderedLocationGroupByEmployee(locationRandomKeys));
                })
                .collect(Collectors.toList());
    }


}
